package com.sql.dao.mapper;

import com.sql.dao.bean.OneTable;

public interface DelectMapper {
	
	/**
	 * 通过主键删除数据
	 * @param obj
	 * @return
	 */
	public Integer delectByID(OneTable obj);

}
