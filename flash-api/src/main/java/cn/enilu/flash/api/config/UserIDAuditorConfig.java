package cn.enilu.flash.api.config;

import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.utils.Constants;
import cn.enilu.flash.utils.HttpUtil;
import cn.enilu.flash.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审计功能配置
 *
 * @author enilu
 * @version 2019/1/8 0008
 */
@Configuration
@Slf4j
public class UserIDAuditorConfig implements AuditorAware<Long> {
    @Override
    @Bean
    public Optional<Long> getCurrentAuditor() {
        try {
            String token = HttpUtil.getRequest().getHeader("Authorization");
            if (StringUtil.isNotEmpty(token)) {
                return Optional.of(JwtUtil.getUserId(token));
            }
        } catch (Exception e) {
            //返回系统用户id
            return Optional.of(Constants.SYSTEM_USER_ID);
        }
        log.info("审计功能开启{}","测试数据");
        //返回系统用户id
        return Optional.of(Constants.SYSTEM_USER_ID);
    }

}
