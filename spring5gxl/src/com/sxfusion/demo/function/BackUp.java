package com.sxfusion.demo.function;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sxfusion.demo.util.MysqlUtil;

public class BackUp implements Job {

	
	private static Logger logger = Logger.getLogger(Testname.class);
	@Override
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		// TODO Auto-generated method stub

		logger.info("数据库定时任务   备份开始");
		MysqlUtil.backup("39.103.237.224", "root", "123456", "C:\\swof", "bolse", "bolse");
		logger.info("数据库定时任务   备份完成");
	}
	
	
	

}
