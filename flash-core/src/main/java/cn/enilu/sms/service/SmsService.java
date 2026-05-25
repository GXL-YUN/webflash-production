package cn.enilu.sms.service;

import cn.enilu.sms.bean.SmsResult;
import cn.enilu.sms.bean.VerifyResult;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsService {

    @Value("${tencent.sms.secret-id}")
    private String secretId;

    @Value("${tencent.sms.secret-key}")
    private String secretKey;

    @Value("${tencent.sms.sdk-app-id}")
    private String sdkAppId;

    @Value("${tencent.sms.sign-name}")
    private String signName;

    @Value("${tencent.sms.template-id}")
    private String templateId;

    @Value("${tencent.sms.region:ap-guangzhou}")
    private String region;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Redis 键前缀
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String SMS_LIMIT_PREFIX = "sms:limit:";
    private static final String SMS_SEND_LOCK_PREFIX = "sms:lock:";

    // 验证码配置
    private static final int CODE_EXPIRE_MINUTES = 5; // 验证码有效期5分钟
    private static final int LIMIT_EXPIRE_MINUTES = 1; // 限流时间1分钟
    private static final int MAX_SEND_PER_MINUTE = 3;  // 每分钟最多发送3次
    private static final int LOCK_EXPIRE_SECONDS = 60; // 发送锁60秒

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return 发送结果
     */
    public SmsResult sendVerificationCode(String phoneNumber) {
        // 1. 验证手机号格式
        if (!isValidPhoneNumber(phoneNumber)) {
            return SmsResult.error("手机号格式错误");
        }

        // 2. 检查发送频率限制
        if (!checkSendLimit(phoneNumber)) {
            return SmsResult.error("发送过于频繁，请稍后再试");
        }

        // 3. 获取分布式锁，防止重复发送
        String lockKey = SMS_SEND_LOCK_PREFIX + phoneNumber;
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            return SmsResult.error("操作过于频繁，请稍后再试");
        }

        try {
            // 4. 生成验证码
            String code = generateRandomCode();
            log.info("为手机号 {} 生成验证码: {}", phoneNumber, code);

            // 5. 发送短信
            //boolean sendSuccess = sendSms(phoneNumber, code);

            if (true) {
                // 6. 存储验证码到Redis
                saveCodeToRedis(phoneNumber, code);

                // 7. 记录发送次数
                recordSendCount(phoneNumber);

                return SmsResult.success("验证码发送成功");
            } else {
                return SmsResult.error("验证码发送失败，请重试");
            }
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * 验证验证码
     * @param phoneNumber 手机号
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    public VerifyResult verifyCode(String phoneNumber, String code) {
        // 1. 参数校验
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return VerifyResult.error("手机号不能为空");
        }

        if (code == null || code.trim().isEmpty()) {
            return VerifyResult.error("验证码不能为空");
        }

        // 2. 从Redis获取验证码
        String redisKey = SMS_CODE_PREFIX + phoneNumber;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        if (storedCode == null) {
            return VerifyResult.error("验证码已过期，请重新获取");
        }

        // 3. 验证码比对（忽略大小写）
        if (storedCode.equalsIgnoreCase(code)) {
            // 验证成功后删除验证码，防止重复使用
            redisTemplate.delete(redisKey);

            // 记录验证成功
            log.info("手机号 {} 验证码验证成功", phoneNumber);

            return VerifyResult.success("验证成功");
        } else {
            // 验证失败处理
            return handleVerifyFailure(phoneNumber, redisKey);
        }
    }

    /**
     * 处理验证失败
     */
    private VerifyResult handleVerifyFailure(String phoneNumber, String redisKey) {
        // 获取错误次数
        String errorKey = SMS_CODE_PREFIX + phoneNumber + ":error";
        Long errorCount = redisTemplate.opsForValue().increment(errorKey, 1);

        if (errorCount == 1) {
            // 第一次错误，设置过期时间
            redisTemplate.expire(errorKey, 10, TimeUnit.MINUTES);
        }

        if (errorCount != null && errorCount >= 5) {
            // 错误次数超过5次，清除验证码
            redisTemplate.delete(redisKey);
            redisTemplate.delete(errorKey);
            return VerifyResult.error("验证失败次数过多，验证码已失效，请重新获取");
        }

        return VerifyResult.error("验证码错误，剩余尝试次数: " + (5 - errorCount));
    }

    /**
     * 检查发送频率限制
     */
    private boolean checkSendLimit(String phoneNumber) {
        String limitKey = SMS_LIMIT_PREFIX + phoneNumber;
        Long sendCount = redisTemplate.opsForValue().increment(limitKey, 1);

        if (sendCount == 1) {
            // 第一次发送，设置过期时间
            redisTemplate.expire(limitKey, LIMIT_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }

        return sendCount != null && sendCount <= MAX_SEND_PER_MINUTE;
    }

    /**
     * 保存验证码到Redis
     */
    private void saveCodeToRedis(String phoneNumber, String code) {
        String key = SMS_CODE_PREFIX + phoneNumber;

        // 存储验证码，设置5分钟过期
        redisTemplate.opsForValue().set(
                key,
                code,
                CODE_EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        // 存储验证码生成时间，用于日志记录
        String timeKey = SMS_CODE_PREFIX + phoneNumber + ":time";
        redisTemplate.opsForValue().set(
                timeKey,
                String.valueOf(System.currentTimeMillis()),
                CODE_EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );
    }

    /**
     * 记录发送次数
     */
    private void recordSendCount(String phoneNumber) {
        String countKey = SMS_CODE_PREFIX + phoneNumber + ":count";
        Long totalCount = redisTemplate.opsForValue().increment(countKey, 1);

        if (totalCount == 1) {
            // 设置24小时过期
            redisTemplate.expire(countKey, 24, TimeUnit.HOURS);
        }

        log.info("手机号 {} 今日发送短信次数: {}", phoneNumber, totalCount);
    }

    /**
     * 发送短信
     */
    private boolean sendSms(String phoneNumber, String code) {
        try {
            // 初始化腾讯云客户端
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            httpProfile.setReqMethod("POST");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, region, clientProfile);

            // 构建请求
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(sdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);

            // 设置手机号（需要加国际码）
            String[] phoneNumbers = {"+86" + phoneNumber};
            req.setPhoneNumberSet(phoneNumbers);

            // 设置模板参数
            String[] templateParams = {code, String.valueOf(CODE_EXPIRE_MINUTES)};
            req.setTemplateParamSet(templateParams);

            // 发送请求
            SendSmsResponse resp = client.SendSms(req);

            // 处理响应
            SendStatus[] sendStatusSet = resp.getSendStatusSet();
            if (sendStatusSet != null && sendStatusSet.length > 0) {
                SendStatus status = sendStatusSet[0];
                boolean success = "Ok".equals(status.getCode());

                if (success) {
                    log.info("短信发送成功 - 手机号: {}, 请求ID: {}",
                            phoneNumber, resp.getRequestId());
                } else {
                    log.error("短信发送失败 - 手机号: {}, 错误码: {}, 错误信息: {}",
                            phoneNumber, status.getCode(), status.getMessage());
                }

                return success;
            }

        } catch (TencentCloudSDKException e) {
            log.error("腾讯云短信发送异常 - 手机号: {}, 错误信息: {}",
                    phoneNumber, e.getMessage(), e);
        } catch (Exception e) {
            log.error("短信发送系统异常 - 手机号: {}", phoneNumber, e);
        }

        return false;
    }

    /**
     * 生成6位随机验证码
     */
    private String generateRandomCode() {
        int code = (int)((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }

    /**
     * 验证手机号格式
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }
        return phoneNumber.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 获取剩余有效期（秒）
     */
    public Long getCodeExpireTime(String phoneNumber) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 清除验证码
     */
    public void clearCode(String phoneNumber) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        redisTemplate.delete(key);

        String timeKey = SMS_CODE_PREFIX + phoneNumber + ":time";
        redisTemplate.delete(timeKey);

        String errorKey = SMS_CODE_PREFIX + phoneNumber + ":error";
        redisTemplate.delete(errorKey);

        log.info("已清除手机号 {} 的验证码", phoneNumber);
    }




}