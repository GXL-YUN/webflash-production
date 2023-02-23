package com.km.task.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.landray.kmss.util.SpringBeanUtil;
import com.sxfusion.demo.function.Testname;
import com.util.time.Main;
import com.util.time.QuartzManager;

@RestController
@RequestMapping("job")
public class TackJob {

	


	private static Logger logger = Logger.getLogger(TackJob.class);

	@Resource
    private QuartzManager quartzManager;

	
    
    /**
     * @Description: 添加一个定时任务
     * 
     * @param jobName
     *            任务名
     * @param jobGroupName
     *            任务组名
     * @param triggerName
     *            触发器名
     * @param triggerGroupName
     *            触发器组名
     * @param jobClass
     *            任务
     * @param cron
     *            时间设置，参考quartz说明文档
     *            jobName=ap1&jobGroupName=ap2&triggerName=ap3&triggerGroupName=ap4&jobClass=com.sxfusion.demo.function.BackUp&cron=0 0 0 1/1 * ?       
    
     */
    
    @RequestMapping(value = "startQuartz", method = {RequestMethod.GET})
    @ResponseBody
    public String testQuartz( String jobName,
    		String jobGroupName,
    		String triggerGroupName,
    		Class<?> jobClass,
    		String triggerName,String quartzTime) throws Exception{
        //logger.info("===========开始执行调度=========时间为 " + quartzTime);
        //String cronStr = quartzManager.transCron(quartzTime);
        //logger.info("======cron表达式========" + quartzTime);
   
       
		quartzManager.addJob(jobName, jobGroupName, triggerName,triggerGroupName,jobClass, quartzTime);    
        //quartzManager.modifyJobTime("test", "test", "test", "test", "0 50 10 * * ?");    
        //quartzManager.removeJob("test", "test", "test", "test");    
        //quartzManager.shutdownJobs();
        return "success";
    }
    /**
     * 关闭定时任务
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "stopQuartz", method = {RequestMethod.GET})
    @ResponseBody
    public String shutDownQuartz() throws Exception{
    	
    	
    	
        logger.info("===========关闭调度test==================");
        quartzManager.removeJob("test", "test", "test", "test");  
        return "关闭成功";
    }

}
