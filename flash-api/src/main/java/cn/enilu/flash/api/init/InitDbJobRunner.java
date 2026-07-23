package cn.enilu.flash.api.init;

import cn.enilu.flash.bean.vo.QuartzJob;
import cn.enilu.flash.service.task.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 需要依赖注入完成后、应用完全启动前执行
 */
@Component
@Slf4j
public class InitDbJobRunner implements CommandLineRunner {


    @Autowired
    private JobService jobService;

    @Override
    public void run(String... args) {
        log.info("init db job start...  MQ定时任务检查初始化");
//        QuartzJob job = new QuartzJob();
//        cfg.setCfgName("default.admin");
//        cfg.setCfgValue("admin");
//        cfg.setDescription("默认管理员标记");
//        cfgService.save(cfg);
        log.info("init db job end...");
    }
}