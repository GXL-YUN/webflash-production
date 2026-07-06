package cn.enilu.flash.api.controller;

import cn.enilu.sms.bean.ApiResult;
import cn.enilu.sms.bean.SmsResult;
import cn.enilu.sms.bean.VerifyRequest;
import cn.enilu.sms.bean.VerifyResult;
import cn.enilu.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/sms")
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 发送验证码
     */
    @PostMapping("/send-code")
    public ApiResult<SmsResult> sendCode(
            @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$",
                    message = "手机号格式错误") String phoneNumber) {

        SmsResult result = smsService.sendVerificationCode(phoneNumber);

        // 开发环境下返回验证码，生产环境不返回
        if (result.isSuccess()) {
            // 这里可以添加开发环境的特殊处理
            // 比如返回固定的测试验证码
        }

        return ApiResult.success(result);
    }

    /**
     * 验证验证码
     */
    @PostMapping("/verify")
    public ApiResult<VerifyResult> verifyCode(
            @RequestBody @Valid VerifyRequest request) {

        VerifyResult result = smsService.verifyCode(
                request.getPhoneNumber(),
                request.getCode()
        );

        if (result.isSuccess()) {
            // 验证成功，可以执行后续业务逻辑
            // 比如用户注册、登录、修改密码等
        }

        return ApiResult.success(result);
    }

    /**
     * 获取验证码剩余有效期
     */
    @GetMapping("/expire-time")
    public ApiResult<Long> getExpireTime(
            @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$",
                    message = "手机号格式错误") String phoneNumber) {

        Long expireTime = smsService.getCodeExpireTime(phoneNumber);
        return ApiResult.success(expireTime);
    }

    /**
     * 清除验证码（调试用）
     */
    @PostMapping("/clear")
    public ApiResult<String> clearCode(
            @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$",
                    message = "手机号格式错误") String phoneNumber) {
        smsService.clearCode(phoneNumber);
        return ApiResult.success("验证码已清除");
    }
}


