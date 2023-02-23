package com.sql.dao.service.icmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.FindModelService;

@Service
public class FindModelServiceIcmp implements FindModelService {

	private static Logger logger = Logger.getLogger(FindModelServiceIcmp.class);
	@Autowired
	private InsertDateMapper i;
	
	@Autowired
	private FindMapper s;
	
	
	@Override
	public List<Map<String, String>> findOlist(OneTable msql) {
		// TODO Auto-generated method stub
	  try {
			List<Map<String, String>> findOlist = s.findOlist(msql);
			return findOlist;
	  }catch (Exception e) {
		// TODO: handle exception
		 logger.error("单表查询失败  错误原因:"+e.getMessage());
	} 
		return 	null;
	}
	
	
	@Override
	public Integer getOcount(OneTable msql) {
		// TODO Auto-generated method stub
		  try {
			  Integer count = s.getOcount(msql);
				return count;
		  }catch (Exception e) {
			// TODO: handle exception
			 logger.error("单表求和失败  错误原因:"+e.getMessage());
		} 
			return 	null;
	}


	@Override
	public List<Map<String ,String>> findByOlink(OneTable msql) {
		// TODO Auto-generated method stub
		try {
			List<Map<String, String>> findOlist = s.findByOlink(msql);
			return findOlist;
	  }catch (Exception e) {
		// TODO: handle exception
		 logger.error("单表主键查询失败  错误原因:"+e.getMessage());
	} 
			return 	null;
	}


	@Override
	public List<Map<String, String>> finNameCount(String msql) {
		// TODO Auto-generated method stub
		try {
			List<Map<String, String>> lists = s.finNameCount(msql);
			List<Map<String, String>>  list=new ArrayList<>();
			if(lists.size()>0) {
			
			for(Map<String, String> a:lists) {
				OneTable table=new OneTable();
				table.setTable(a.get("table_name"));
				Integer ocount = this.getOcount(table);
				Map<String, String> date=new HashMap<>();
				date.put("name", a.get("table_name"));
				date.put("count", ocount.toString());
				list.add(date);
				}
			}
			return list;
	  }catch (Exception e) {
		// TODO: handle exception
		 logger.error("查询失败  错误原因:"+e.getMessage());
	} 
		return null;
	}


}
