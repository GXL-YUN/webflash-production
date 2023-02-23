package com.km.task.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.km.doument.entity.Test;

import com.km.task.entity.Task;
import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.DelectService;
import com.sql.dao.service.FindModelService;
import com.sql.dao.service.InsertService;
import com.sxfusion.demo.entity.Music;

import fr.opensagres.xdocreport.document.json.JSONArray;


@RestController
public class TaskTime {
	
	//数据访问层
	@Autowired
	private FindModelService findModelService;
	
	
	@Autowired
	private DelectService delectService;
	

	@Autowired
	private InsertService insertService;
	
	
	
	@RequestMapping("selectTack")
	@ResponseBody
	public List selectTack(OneTable msql) {
		
		msql.setTable("task_timeing");
		List<?> list = findModelService.findOlist(msql);
		System.out.println(findModelService.finNameCount("test"));
		return list;
	}
	
	@RequestMapping("findtackbyid")
	@ResponseBody
	public List findtackbyid(OneTable msql) {
		
		msql.setTable("task_timeing");
		List<?> list = findModelService.findByOlink(msql);
		return list;
	}
	
	@RequestMapping("tackcount")
	@ResponseBody
	public Integer tackcount(OneTable msql) {
		
		msql.setTable("task_timeing");
		 Integer ocount = findModelService.getOcount(msql);
		return ocount;
	}
	
	/**
	 * 按键值进行删除    
	 * @param msql
	 * @return
	 */
	@RequestMapping("deletebytype")
	@ResponseBody
	public String deletebytype(OneTable msql) {
		msql.setTable("task_timeing");
		String ocount = delectService.delectByID(msql);
		return  ocount;
	}
	
	/**
	 * 添加数据
	 */
	@RequestMapping("inserttype")
	@ResponseBody
	public void inserttype(Task msql) {
		msql.setTable("task_timeing");
		boolean ocount = insertService.insert(msql.modeltoMap(msql));
		//return  ocount;
	}	
	
	
	/**总数据可视化
	 * 添加数据zo@return 
	 */
	@RequestMapping("count")
	@ResponseBody
	public List<Map<String, String>> count(Task msql) {
		try {
			return  findModelService.finNameCount("test");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 启动一个线程  实现长链接数据
	 * @param msql
	 * @return
	 */
	@RequestMapping("thread")
	@ResponseBody
	public List<Map<String, String>> thread(Task msql) {
		return  findModelService.finNameCount("test");
	}
	
}
