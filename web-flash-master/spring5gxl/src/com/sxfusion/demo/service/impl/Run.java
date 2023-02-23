package com.sxfusion.demo.service.impl;


import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.km.task.controller.TackJob;
import com.km.task.controller.TaskTime;
import com.sql.dao.bean.OneTable;
import com.sql.dao.service.FindModelService;
import com.sql.dao.service.icmp.FindModelServiceIcmp;
import com.sun.javafx.collections.MappingChange.Map;
import com.sxfusion.demo.function.Testname;
import com.sxfusion.time.Test;
import com.util.time.Main;


/**
 * 项目启动是  启动定时任务
 * @author gxl
 *
 */

@Service
public class Run{
	
	
	
	@Autowired
	private TackJob TackJob;
	
	@Autowired
	private TaskTime taskTime;
	@Autowired
	private FindModelService findModelService;
	//实例单表查询
	OneTable msql =new OneTable();
	/**
	 * 第一个
	 */
	/*
	 * @PostConstruct public void start(){
	 * 
	 * Test test=new Test(); Main.test(test); }
	 */
	/**
	 * 第二个
	 */
	private static Logger logger = Logger.getLogger(Run.class);
	@PostConstruct 
	public void starts(){
		
		
	    msql.setWhere("fd_start=0");
	    msql.setTable("task_timeing");
	    List<java.util.Map<String, String>>  list = findModelService.findOlist(msql);
		
		
		logger.info("定时任务接口执行数量"+list.size());

		logger.info("定时任务开始启动");
		int  i=0;
		if(list.size()>0)	{
		for(java.util.Map<String, String> a:  list) {
			try {
				Class<?> forName = Class.forName(a.get("fd_class"));
				TackJob.testQuartz(a.get("fd_name"),a.get("fd_groupa"),a.get("fd_groupb"),forName,a.get("fd_groupc"),a.get("fd_time"));
			i++;
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("定时任务开始启动失败" +a.get("fd_name")+a.get("fd_class") );
			}finally {
				logger.info("定时任务开始启动成功 第" +i +"个  名称 ：路径" +a.get("fd_name")+a.get("fd_class") );
			}
		}
		}
	  //Main.test("com.sxfusion.demo.function.Testname","0 56 0 * * ?" );
	   
	}
}