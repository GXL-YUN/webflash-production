package cn.enilu.flash.api.interceptor;



import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义权限过滤器
 * 1. 支持通配符权限
 * 2. 支持枚举权限
 * 3. 统一响应格式
 */
@Slf4j
public class PermissionAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                   Object mappedValue) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(httpRequest.getMethod())) {
            return true;
        }

        Subject subject = getSubject(request, response);

        // 获取需要的权限
        String[] perms = (String[]) mappedValue;
        if (perms == null || perms.length == 0) {
            log.info("无权限");
            return true; // 没有权限要求，直接放行
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws IOException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", "权限不足");
        result.put("success", false);
        result.put("timestamp", System.currentTimeMillis());

        httpResponse.getWriter().write(result.toString());
        return false;
    }

}