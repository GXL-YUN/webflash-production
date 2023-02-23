package com.sql.dao.mapper;
/**
 * 单表数据更新
 * 
 * @author Lenovo
 *
 */
public interface UpdateMapper {
	
	/**
	 * 图片更新   通过 id  table 变量进行数据变革
	 */
      public  boolean change(String id ,String table ) ;
	
	
}
