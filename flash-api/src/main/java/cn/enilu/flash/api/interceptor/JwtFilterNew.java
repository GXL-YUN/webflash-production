package cn.enilu.flash.api.interceptor;

import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.security.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT过滤器
 */
@Slf4j
public class JwtFilterNew extends AccessControlFilter {

    /**
     * 判断是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        // 这里直接返回false，由onAccessDenied处理
        return false;
    }

    /**
     * 当访问被拒绝时的处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 放行OPTIONS请求
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpResponse.setStatus(HttpStatus.OK.value());
            return true;
        }

        // 获取Token
        String token = getRequestToken(httpRequest);
        if (token == null) {
            log.warn("请求未携带Token: {}", httpRequest.getRequestURI());
            unauthorizedResponse(httpResponse, "请先登录");
            return false;
        }

        // 简单验证Token格式
        if (!isValidTokenFormat(token)) {
            log.warn("Token格式无效: {}", token);
            unauthorizedResponse(httpResponse, "Token格式无效");
            return false;
        }

        // 创建AuthenticationToken
        AuthenticationToken authenticationToken = createToken(token);

        try {
            // 执行登录
            getSubject(request, response).login(authenticationToken);
            //log.info("认证成功: {}", httpRequest.getRequestURI());
            return true;
        } catch (UnknownAccountException e) {
            log.warn("账号不存在: {}", e.getMessage());
            unauthorizedResponse(httpResponse, "账号不存在");
            return false;
        } catch (IncorrectCredentialsException e) {
            log.warn("凭证错误: {}", e.getMessage());
            unauthorizedResponse(httpResponse, "用户名或密码错误");
            return false;
        } catch (AuthenticationException e) {
            log.warn("认证失败: {}", e.getMessage());
            unauthorizedResponse(httpResponse, "认证失败: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("认证异常: {}", e.getMessage(), e);
            unauthorizedResponse(httpResponse, "系统异常，请稍后重试");
            return false;
        }
    }

    /**
     * 验证Token格式
     */
    private boolean isValidTokenFormat(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        // JWT Token通常是三段，用点分隔
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    /**
     * 从请求中获取Token
     */
    private String getRequestToken(HttpServletRequest request) {
        // 从Header获取
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        // 从参数获取
        token = request.getParameter("token");
        if (token != null && !token.trim().isEmpty()) {
            return token;
        }

        return null;
    }

    /**
     * 创建AuthenticationToken
     */
    private AuthenticationToken createToken(String token) {
        return new JwtToken(token);
    }

    /**
     * 设置未授权响应
     */
    private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Ret rets = Rets.failure(401+message);
        response.getWriter().write(401+message);
    }

    /**
     * 设置CORS头部
     */
    private void setCorsHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}