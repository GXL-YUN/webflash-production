package cn.enilu.flash.api.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 随 Spring 启动
 * 生产环境最常用
 */
@Component
@Slf4j
public class InitDbRunner {



    @PostConstruct
    public void init() {
        // 判断是否已经存在
        log.info("init db job start...  MQ定时任务检查初始化1");
//        QuartzJob job = new QuartzJob();
//        cfg.setCfgName("default.admin");
//        cfg.setCfgValue("admin");
//        cfg.setDescription("默认管理员标记");
//        cfgService.save(cfg);
        log.info("init db job end...2");
    }
}