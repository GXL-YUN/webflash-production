package com.km.pic.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.km.doument.entity.Blogs_document;
import com.km.url.entity.Bolse_web;
import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.service.DelectService;
import com.sql.dao.service.FindModelService;
import com.sql.dao.service.InsertService;

import fr.opensagres.xdocreport.document.json.JSONArray;
/**
 * 由于本类数据是由定时任务调用所以调用定时任务中的数据
 * 
 * 
 * @author Lenovo
 *
 */
@RestController
@RequestMapping("pic")
public class PicContollen {
	
	//数据访问层
		@Autowired
		private FindModelService findModelService;
		
		
		@Autowired
		private DelectService delectService;
		

		@Autowired
		private InsertService insertService;
		
		// 设置表名
		private  String  table ="pic_main"; 
		
		@RequestMapping("select")
		@ResponseBody
		public List selectTack(OneTable msql) {
			
			msql.setTable(table);
			List<?> list = findModelService.findOlist(msql);
			return list;
		}
		
		@RequestMapping("findid")
		@ResponseBody
		public List findtackbyid(OneTable msql) {
			
			msql.setTable(table);
			List<?> list = findModelService.findByOlink(msql);
			return list;
		}
		
		@RequestMapping("count")
		@ResponseBody
		public Integer tackcount(OneTable msql) {
			msql.setTable(table);
			 Integer ocount = findModelService.getOcount(msql);
			return ocount;
		}
		
		/**
		 * 按键值进行删除    
		 * @param msql
		 * @return
		 */
		@RequestMapping("delete")
		@ResponseBody
		public String deletebytype(OneTable msql) {
			msql.setTable(table);
			String ocount = delectService.delectByID(msql);
			return  ocount;
		}
		
		/**
		 * 添加数据       空缺
		 */
		@RequestMapping("insert")
		@ResponseBody
		public void inserttype(Bolse_web msql) {
			
			msql.setTable("");
			boolean ocount = insertService.insert(msql.modeltoMap(msql));
			//return  ocount;
		}
	
}
