// ApiLogAspect.java
package cn.enilu.aop;


import cn.enilu.log.bean.model.ApiLog;
import cn.enilu.log.service.ApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    @Autowired(required = false)
    private ApiLogService apiLogService;

    // 最大记录长度
    private static final int MAX_BODY_LENGTH = 5000;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void apiPointcut() {}

    @Around("apiPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString().replace("-", "");

        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        // 记录基本信息
        ApiLog apiLog = new ApiLog();
        apiLog.setRequestId(requestId);
        apiLog.setMethod(request.getMethod());
        apiLog.setUri(request.getRequestURI());
        apiLog.setQueryParams(request.getQueryString());
        apiLog.setClientIp(getClientIp(request));
        apiLog.setUserAgent(request.getHeader("User-Agent"));


        Object result = null;
        Integer statusCode = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 获取响应状态码
            if (response != null) {
                statusCode = response.getStatus();
                apiLog.setStatusCode(statusCode);
                apiLog.setSuccess(statusCode < 400);
            }

            return result;

        } catch (Exception e) {
            statusCode = 500;
            apiLog.setStatusCode(statusCode);
            apiLog.setSuccess(false);
            apiLog.setErrorMessage(e.getMessage());
            log.error("接口执行异常: {} {}, 异常: {}", apiLog.getMethod(), apiLog.getUri(), e.getMessage());
            throw e;

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            apiLog.setDuration(duration);
            //apiLog.setCreateTime(LocalDateTime.now());

            try {
                // 记录请求体和响应体
                recordRequestBody(request, apiLog);
                recordResponseBody(response, apiLog);

                // 获取请求体
//                if (request instanceof ContentCachingRequestWrapper) {
//                    apiLog.setRequestBody(getRequestBody((ContentCachingRequestWrapper) request));
//                }
                result = joinPoint.proceed();

                // 获取响应信息
                if (result instanceof ResponseEntity) {
                    ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                    apiLog.setStatusCode(responseEntity.getStatusCodeValue());
                }

                // 记录响应体
                apiLog.setResponseBody(result.toString());

                // 保存日志
                saveLog(apiLog);

            } catch (Exception e) {
                // 日志记录异常不影响主流程
                log.debug("记录API日志异常: {}", e.getMessage());
            }
        }
    }

    /**
     * 记录请求体
     */
    private void recordRequestBody(HttpServletRequest request, ApiLog apiLog) {
        try {
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                byte[] content = wrapper.getContentAsByteArray();

                if (content != null && content.length > 0) {
                    // 获取字符编码
                    String encoding = wrapper.getCharacterEncoding();
                    String charsetName = (encoding != null && !encoding.trim().isEmpty())
                            ? encoding
                            : StandardCharsets.UTF_8.name();

                    // 转换为字符串
                    String requestBody = new String(content, charsetName);

                    // 限制长度
                    if (requestBody.length() > MAX_BODY_LENGTH) {
                        requestBody = requestBody.substring(0, MAX_BODY_LENGTH) + "...[截断]";
                    }

                    apiLog.setRequestBody(requestBody);
                }
            }
        } catch (Exception e) {
            log.warn("记录请求体失败: {}", e.getMessage());
        }
    }

    /**
     * 记录响应体
     */
    private void recordResponseBody(HttpServletResponse response, ApiLog apiLog) {
        try {
            if (response instanceof ContentCachingResponseWrapper) {
                ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
                byte[] content = wrapper.getContentAsByteArray();

                if (content != null && content.length > 0) {
                    // 获取字符编码
                    String encoding = wrapper.getCharacterEncoding();
                    String charsetName = (encoding != null && !encoding.trim().isEmpty())
                            ? encoding
                            : StandardCharsets.UTF_8.name();

                    // 转换为字符串
                    String responseBody = new String(content, charsetName);

                    // 限制长度
                    if (responseBody.length() > MAX_BODY_LENGTH) {
                        responseBody = responseBody.substring(0, MAX_BODY_LENGTH) + "...[截断]";
                    }

                    apiLog.setResponseBody(responseBody);

                    // 重要：复制响应体到原始响应
                    wrapper.copyBodyToResponse();
                }
            }
        } catch (Exception e) {
            log.warn("记录响应体失败: {}", e.getMessage());
        }
    }

    /**
     * 保存日志
     */
    private void saveLog(ApiLog apiLog) {
        if (apiLogService != null) {
            apiLogService.saveAsync(apiLog);
        } else {
            // 打印简单日志
            log.info("API日志[{}]: {} {} -> {}ms, 状态: {}",
                    apiLog.getRequestId(),
                    apiLog.getMethod(),
                    apiLog.getUri(),
                    apiLog.getDuration(),
                    apiLog.getStatusCode() != null && apiLog.getStatusCode() < 400 ? "成功" : "失败"
            );
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}