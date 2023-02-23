package com.km.context.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.km.doument.entity.Test;
import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.FindModelService;
import com.sxfusion.demo.entity.Music;

import fr.opensagres.xdocreport.document.json.JSONArray;


@RestController
public class Activedate {
	
	//数据访问层
	@Autowired
	private FindModelService s;
	
	
	@Autowired
	private InsertDateMapper i;
	 //第一次测试数据
	
	@RequestMapping("selectindex")
	@ResponseBody
	public List selectUtTstUserByName(String name) {
		OneTable msql=new OneTable();
		msql.setTable("document_main");
		
		msql.setWhere("java");
	
		 List<?> list = s.findOlist(msql);
		
	
		//System.out.println("测试"+test.toString());
		
		return list;
	} 
	//查询标题数据
	@RequestMapping("selectcount") 
	@ResponseBody
	public List selectcount(String name) {
		OneTable msql=new OneTable();
		msql.setTable("doument_main");
		List<?> list = s.findOlist(msql);
        JSONArray test=new JSONArray();	
        test.add(list);
		return test;
	}
	
	@RequestMapping("ins")
	@ResponseBody
	public boolean ins(Test bloment) {
		/*
		 * bloment.setTable("doument_main"); //添加草案u哦 boolean insert =
		 * i.insert(bloment.modeltoMap(bloment)); System.out.println("boolean"+insert);
		 */
		Music m=new Music();
		m.setTable("pic_main");	
		
		System.out.println("捕获");
		boolean s = i.insert(m.modeltoMap(m));
		System.out.println("boolean"+s);
		
		return s;
	}

}
