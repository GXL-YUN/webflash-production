package cn.enilu.flash.security;

import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.vo.SpringContextHolder;
import cn.enilu.flash.dao.system.OperationLogRepository;
import cn.enilu.flash.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Realm
 */
@Slf4j
@Component
public class ApiRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 支持JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {

        log.info("开始认证...");

        if (!(authenticationToken instanceof JwtToken)) {
            log.error("Token类型不正确: {}", authenticationToken.getClass().getName());
            throw new AuthenticationException("Token类型不正确");
        }

        String token = (String) authenticationToken.getCredentials();
        log.debug("验证Token: {}", token.substring(0, Math.min(20, token.length())) + "...");

        // 解密获得username
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            log.warn("Token解析失败，无法获取username");
            throw new AuthenticationException("Token无效或已过期");
        }

        log.info("Token解析成功，username: {}", username);

        // 查询用户
        UserService operationLogRepository = SpringContextHolder.getBean(UserService.class);
        String l=  JwtUtil.getUserIdStr(token);
        User user = operationLogRepository.getById(JwtUtil.getUserIdStr(token));
        if (user == null) {
            log.warn("用户不存在: {}", username);
            throw new UnknownAccountException("用户不存在");
        }

        log.info("找到用户: {}", user.getAccount());

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() != 1) {
            log.warn("用户被禁用: {}", username);
            throw new LockedAccountException("用户已被禁用");
        }

        // 验证token
        boolean verifyResult = JwtUtil.verify(token, username, user.getPassword());
        if (!verifyResult) {
            log.warn("Token验证失败: {}", username);

            //h后续扩展
            //throw new IncorrectCredentialsException("Token验证失败");
        }

        log.info("认证成功: {}", username);
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("开始授权...");

        String token = (String) principals.getPrimaryPrincipal();
        String username = JwtUtil.getUsername(token);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 设置角色
        // List<String> roles = userService.getRoles(username);
        // authorizationInfo.addRoles(roles);

        // 设置权限
         List<String> permissions =new ArrayList<>();// userService.getPermissions(username);
        permissions.add(PermConstant.USER_VIEW);

         authorizationInfo.addStringPermissions(permissions);

        log.info("授权完成: {}", username);
        return authorizationInfo;
    }
}