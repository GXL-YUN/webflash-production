package com.sxfusion.demo.function;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.km.context.controller.Activedate;
import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.FindModelService;
import com.sql.dao.service.icmp.FindModelServiceIcmp;
import com.sxfusion.demo.entity.Music;
import com.util.ApplicationContextUtil;
import com.util.HttpClientT;
import com.util.HttpsUtil;
import com.util.time.Main;

/**
 * 任务模板数据
 * @author Lenovo
 *
 */
public class Testname implements Job{
	
	@Autowired
	private InsertDateMapper i;
	//数据访问层
	@Autowired
	private FindModelService s;
	
	private static Logger logger = Logger.getLogger(Testname.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		try {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		JSONObject doGet=null;
		try {
			doGet = HttpsUtil.get("https://api.iyk0.com/yi","ss");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			logger.error("接口调用出错");
		}
	      String jsonArray = doGet.getString("data");
	      
	          Msql msql=new Msql();
			  msql.setTable("doument_main");
			  try {
				  Music music=new Music();
				  music.setTable("pic_main");
				  music.setFd_name(jsonArray);
				  boolean insert = i.insert(music.modeltoMap(music));
			  }catch (Exception e) {
				// TODO: handle exception
				  logger.error("数据插入出错");
				  e.printStackTrace();  
			}
		       //音乐类
			  logger.error("接口调用完成");
		}catch (Exception e) {
			// TODO: handle exception
			  logger.error("接口不成功");
		}
	}

}
