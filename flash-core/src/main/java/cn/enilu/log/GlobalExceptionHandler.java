package cn.enilu.log;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SocketException.class)
    public ResponseEntity<Map<String, Object>> handleSocketException(SocketException e) {
        log.error("Socket连接异常: {}", e.getMessage(), e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "网络连接异常，请稍后重试");
        response.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException e) {
        log.error("数据访问异常: {}", e.getMessage(), e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "数据服务暂时不可用，请稍后重试");
        response.put("error", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "系统内部错误");
        response.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
