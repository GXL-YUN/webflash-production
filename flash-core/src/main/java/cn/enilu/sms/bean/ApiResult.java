package cn.enilu.sms.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

// API统一响应
@Data
@AllArgsConstructor
public class ApiResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "success", data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data);
    }

    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }
}