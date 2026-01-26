package cn.enilu.flash.api;

import cn.enilu.flash.dao.BaseRepositoryFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * ApiApplication
 *
 * @author enilu
 * @version 2018/9/11 0011
 */
@EnableCaching
@SpringBootApplication(
        //集成activit后，默认引入了springSecurity，这里需要通过下面配置去掉SpringSecurity
        exclude = {
                SecurityAutoConfiguration.class,
                ManagementWebSecurityAutoConfiguration.class,
        }
)
@EnableAsync  //开启Spring异步线程@Async//需要使用异步处理的方法名
@ComponentScan(basePackages = "cn.*.*")
@EntityScan(basePackages = "cn.enilu.*.*")  //扫描bean包
@EnableJpaRepositories(basePackages = "cn.enilu.*.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableJpaAuditing
@EnableOpenApi
@Controller
public class ApiApplication extends SpringBootServletInitializer {
    private static Logger logger = LoggerFactory.getLogger(ApiApplication.class);
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }
    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;

    private static String GXL_USER_LOGIN_TOKE="GXL:APP:CONFIG:MASSAGE:";
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("开始启动");
        ConfigurableApplicationContext application = SpringApplication.run(ApiApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String active = env.getProperty("spring.profiles.active");
        String port = env.getProperty("server.port");
        port = port == null ? "8080" : port;
        String path = env.getProperty("server.servlet.context-path");
        path = path == null ? "" : path;

        logger.info("\n----------------------------------------------------------\n\t" +
                "web-flash is running! \n\t" +
                "系统运行环境 : \t" + active + "\n\t" +
                "本地访问地址 : \thttp://localhost:" + port + path + "/\n\t" +
                "外部访问地址 : \thttp://" + ip + ":" + port + path + "/\n\t" +
                "在线文档地址 : \thttp://" + ip + ":" + port + path + "/swagger-ui/index.html\n" +
                "----------------------------------------------------------");
                }
}