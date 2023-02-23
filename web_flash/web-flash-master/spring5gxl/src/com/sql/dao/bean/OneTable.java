package com.sql.dao.bean;

import com.github.pagehelper.util.StringUtil;

/**
 * 单表查询数据
 * @author Lenovo
 *
 */
public class OneTable {
	
	private String table; // 表名
	private String where; // 条件
	private int action; // 开始标签
	private int end; // 结束标签
	private String varcher; //查询字段
	
	
	
	public String getVarcher() {
		if(this.varcher==null) {
			varcher="*";
			return varcher;
		}
		return varcher;
	}
	
	/**
	 * 多个字段用逗号隔开  ，，，，
	 * @param varcher
	 */
	public void setVarcher(String varcher) {
		this.varcher = varcher;
	}
	private String fd_id;
 	
	private int index;
	private int count;
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public int getAction() {
		if(index!=0) {
			action=(index-1)*count;
		}
		
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getFd_id() {
		return fd_id;
	}
	public void setFd_id(String fd_id) {
		this.fd_id = fd_id;
	}
	

}
