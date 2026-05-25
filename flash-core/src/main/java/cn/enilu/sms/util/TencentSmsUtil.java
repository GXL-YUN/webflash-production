package cn.enilu.sms.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TencentSmsUtil {

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

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String phoneNumber, String code) {
        try {
            // 初始化认证对象
            Credential cred = new Credential(secretId, secretKey);

            // 配置HTTP Profile
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            httpProfile.setReqMethod("POST");

            // 配置客户端Profile
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            // 初始化SMS客户端
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            // 构建请求参数
            SendSmsRequest req = new SendSmsRequest();

            // 设置应用ID
            req.setSmsSdkAppId(sdkAppId);

            // 设置签名名称
            req.setSignName(signName);

            // 设置模板ID
            req.setTemplateId(templateId);

            // 设置手机号数组（国际号码需加国家码，如+86）
            String[] phoneNumbers = {"+86" + phoneNumber};
            req.setPhoneNumberSet(phoneNumbers);

            // 设置模板参数（根据模板中的占位符顺序）
            String[] templateParams = {code, "5"}; // 验证码和有效期（分钟）
            req.setTemplateParamSet(templateParams);

            // 发送请求
            SendSmsResponse resp = client.SendSms(req);

            // 处理响应
            SendStatus[] sendStatusSet = resp.getSendStatusSet();
            if (sendStatusSet != null && sendStatusSet.length > 0) {
                SendStatus status = sendStatusSet[0];
                return "Ok".equals(status.getCode());
            }

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送营销短信
     * @param phoneNumbers 手机号数组
     * @param templateId 模板ID
     * @param templateParams 模板参数
     * @return 发送结果
     */
    public SendSmsResponse sendMarketingSms(String[] phoneNumbers,
                                            String templateId,
                                            String[] templateParams) {
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            SendSmsRequest req = new SendSmsRequest();

            // 批量发送时，手机号需要加国际码
            String[] formattedNumbers = Arrays.stream(phoneNumbers)
                    .map(num -> num.startsWith("+") ? num : "+86" + num)
                    .toArray(String[]::new);

            req.setPhoneNumberSet(formattedNumbers);
            req.setSmsSdkAppId(sdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);
            req.setTemplateParamSet(templateParams);

            return client.SendSms(req);

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拉取短信回执状态
     */
    public PullSmsSendStatusResponse pullSendStatus(long limit) {
        try {
            Credential cred = new Credential(secretId, secretKey);

            SmsClient client = new SmsClient(cred, "ap-guangzhou");

            PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
            req.setLimit(limit);
            req.setSmsSdkAppId(sdkAppId);

            return client.PullSmsSendStatus(req);

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 统计短信发送数据
     */
    public SendStatusStatisticsResponse getSendStatistics(String startDate, String endDate) {
        try {
            Credential cred = new Credential(secretId, secretKey);

            SmsClient client = new SmsClient(cred, "ap-guangzhou");

            SendStatusStatisticsRequest req = new SendStatusStatisticsRequest();
            req.setBeginTime(startDate);
            req.setEndTime(endDate);
            req.setSmsSdkAppId(sdkAppId);
            req.setLimit(100L);
            req.setOffset(0L);

            return client.SendStatusStatistics(req);

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return null;
        }
    }
}