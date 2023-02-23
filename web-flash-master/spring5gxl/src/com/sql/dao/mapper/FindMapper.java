package com.sql.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sql.dao.bean.Msql;
import com.sql.dao.bean.OneTable;

/**
 * 
 * @author gxl
 *
 */

@Mapper
public  interface FindMapper {
	

	/**
	 * 多对一查询数据集
	 * @param msql
	 * @return
	 */
	public List<Map<String ,String>> findTList(Msql msql);
	/**
	  * 多对一查询数据集//求总数 
	 * @param msql
	 * @return
	 */
	public Integer getTcount(Msql msql);
	

	
	/**
	 * 单表查询
	 * @param OneTable
	 * @return
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
	 * 查询所有数据库中数据名
	 * select table_name from information_schema.tables where table_schema='test'
	 */
	public   List<Map<String ,String>> finNameCount(@Param("msql")String msql);
}
