package cn.enilu.sms.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
/**
 * 验证码发送结果封装
 */
@Data
@AllArgsConstructor
public  class SmsResult {
    private boolean success;
    private String message;
    private String code; // 开发环境返回验证码

    public static SmsResult success(String message) {
        return new SmsResult(true, message, null);
    }

    public static SmsResult error(String message) {
        return new SmsResult(false, message, null);
    }

    // 开发环境使用，返回验证码
    public static SmsResult successWithCode(String message, String code) {
        return new SmsResult(true, message, code);
    }
}