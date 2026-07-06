package cn.enilu.sms.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 验证结果封装
 */
@Data
@AllArgsConstructor
public  class VerifyResult {
    private boolean success;
    private String message;

    public static VerifyResult success(String message) {
        return new VerifyResult(true, message);
    }

    public static VerifyResult error(String message) {
        return new VerifyResult(false, message);
    }
}