// RequestHelper.java
package cn.enilu.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

@Slf4j
public class RequestHelper {

    /**
     * 安全地获取字符编码
     */
    public static Charset getCharset(HttpServletRequest request) {
        try {
            String encoding = request.getCharacterEncoding();
            if (encoding != null && !encoding.trim().isEmpty()) {
                return Charset.forName(encoding);
            }
        } catch (UnsupportedCharsetException e) {
            log.warn("不支持的字符编码: {}, 使用UTF-8", request.getCharacterEncoding());
        }
        return StandardCharsets.UTF_8;
    }

    /**
     * 安全地获取请求体内容
     */
    public static String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] content = wrapper.getContentAsByteArray();
            if (content != null && content.length > 0) {
                try {
                    Charset charset = getCharset(request);
                    return new String(content, charset);
                } catch (Exception e) {
                    log.warn("解析请求体失败: {}", e.getMessage());
                    return new String(content, StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }

    /**
     * 安全地获取请求体内容（带长度限制）
     */
    public static String getRequestBodyLimited(HttpServletRequest request, int maxLength) {
        String body = getRequestBody(request);
        if (body != null && body.length() > maxLength) {
            return body.substring(0, maxLength) + "...[截断]";
        }
        return body;
    }

    /**
     * 安全地获取响应体内容
     */
    public static String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] content = wrapper.getContentAsByteArray();
            if (content != null && content.length > 0) {
                try {
                    String encoding = wrapper.getCharacterEncoding();
                    Charset charset = (encoding != null && !encoding.trim().isEmpty())
                            ? Charset.forName(encoding)
                            : StandardCharsets.UTF_8;
                    return new String(content, charset);
                } catch (Exception e) {
                    log.warn("解析响应体失败: {}", e.getMessage());
                    return new String(content, StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }

    /**
     * 安全地获取响应体内容（带长度限制）
     */
    public static String getResponseBodyLimited(HttpServletResponse response, int maxLength) {
        String body = getResponseBody(response);
        if (body != null && body.length() > maxLength) {
            return body.substring(0, maxLength) + "...[截断]";
        }
        return body;
    }
}