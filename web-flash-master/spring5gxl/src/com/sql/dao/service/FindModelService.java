package com.sql.dao.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;



/**
 * 查询基类
 * @author Lenovo
 *
 */
@Service
public interface FindModelService {
	
	/**
	 * 单表查询
	 */
	public List<Map<String ,String>> findOlist(OneTable msql);
	

	/**
	 *单表求和
	 * @param OneTable
	 * @return
	 */
	public Integer getOcount(OneTable msql);
	
	/**
	 * 单表按主键查询  单条查询
	 * @param msql
	 * @return
	 */
	public List<Map<String ,String>> findByOlink(OneTable msql);	
	
	/**
	 * 查询所有数据库中数据名 及数量
	 * select table_name from information_schema.tables where table_schema='test'
	 */
	public List<Map<String ,String>> finNameCount(String msql);	
	
	
	
}
