package com.util.time;

import org.apache.ibatis.logging.Log;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sql.dao.service.icmp.FindModelServiceIcmp;
import com.sxfusion.demo.function.Testname;
import com.sxfusion.demo.util.JobFactory;
import com.sxfusion.time.Test;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DateBuilder.*;
/**
 * 定时任务
 * 
 * 两种方式  一种  配置   一种类调用 
 * @author Lenovo
 *
 */

public class Main {
	
	private static Logger logger = Logger.getLogger(Main.class);
	public static void test(Object cla,String time ) {
		/**
		 * 两种方式   一种是  配置 一种是Java代码
		 * 
		 */
		
		JobDetail build = null;
		try {
			build = JobBuilder.newJob((Class<? extends Job>) Class.forName((String) cla)).build();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//创建触发器（trigger）对象，并且设置触发规则，多少时间执行一次job
		//SimpleTrigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10000)).build();
		//创建任务调度对象
		
		Trigger  trigger = newTrigger()
				    .withIdentity("trigger3", "group1")
				    .withSchedule(cronSchedule(time))
				    .forJob(build)
				    .build();
		JobFactory jobFactory=new JobFactory();
		//bean无法注入问题
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			//scheduler.setJobFactory(jobFactory);
			//4.将任务和触发器联系到
			scheduler.scheduleJob(build, trigger);
			//5.启动任务
			
			scheduler.start();
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logger.error("TestQuartz end.......定时任务出错");
			e.printStackTrace();
		}finally {
			logger.error("TestQuartz start.......定时任务开启");
		}
		//ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-quartz.xml");
		//如果配置文件中将startQuertz bean的lazy-init设置为false 则不用实例化
	//	context.getBean("taskManager");
	}
	
	

	
	//代用方式
	 public static void main(String[] arg) throws SchedulerException {
	        System.out.println("开启"); 
	        Testname test=new Testname();
	        Main.test("com.sxfusion.demo.function.Testname","0 53 0 * * ?" );
	        
	    }
}
