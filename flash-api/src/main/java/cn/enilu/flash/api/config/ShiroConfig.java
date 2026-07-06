package cn.enilu.flash.api.config;

import cn.enilu.flash.api.interceptor.JwtFilterNew;
import cn.enilu.flash.security.ApiRealm;
import cn.enilu.flash.api.interceptor.PermissionAuthorizationFilter;
import cn.enilu.flash.security.SystemLogoutFilter;
import cn.enilu.flash.utils.Maps;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 */
@Configuration
public class ShiroConfig {


    /**
     * 配置SecurityManager - 必须命名为securityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();


        ApiRealm apiRealm=new ApiRealm();
        // 设置自定义Realm
        securityManager.setRealm(apiRealm);

        // 关闭Shiro自带的Session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    /**
     * 配置Shiro过滤器
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        DefaultWebSecurityManager securityManager = securityManager();

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");

        // 配置自定义过滤器
        Map<String, Filter> filters = Maps.newHashMap();
        filters.put("jwt", new JwtFilterNew());
        filters.put("logout", new SystemLogoutFilter());
        filters.put("perms", new PermissionAuthorizationFilter());
        factoryBean.setFilters(filters);




        // 配置过滤规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();


        //登录界面 // 认证相关/account/loginWxml
        filterChainDefinitionMap.put("/api/account/*/*", "anon");
        filterChainDefinitionMap.put("/account/login", "anon");
        filterChainDefinitionMap.put("/account/logout", "logout");
        filterChainDefinitionMap.put("/account/captcha", "anon");

        // 静态资源和文档（完全开放）
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/v3/api-docs/**", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");

        // Druid监控
        filterChainDefinitionMap.put("/druid/**", "anon");

        //腾讯文档配回调
        filterChainDefinitionMap.put("/api/tencent/callback", "anon");


        // 文件相关
        filterChainDefinitionMap.put("/file/att/**", "anon");
        filterChainDefinitionMap.put("/api/fileUtil/**", "anon");

        // 公开接口
        filterChainDefinitionMap.put("/ekp_mkpass/back/EHS/multiplant/dateAll", "anon");
        filterChainDefinitionMap.put("/api/test/**", "anon");
        filterChainDefinitionMap.put("/api/public/**", "anon");

        // 错误页面
        filterChainDefinitionMap.put("/401", "anon");
        filterChainDefinitionMap.put("/404", "anon");
        filterChainDefinitionMap.put("/500", "anon");

        // 默认规则：所有请求都需要JWT认证
        filterChainDefinitionMap.put("/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }

    /**
     * 启用Shiro注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 启用权限注解
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
}