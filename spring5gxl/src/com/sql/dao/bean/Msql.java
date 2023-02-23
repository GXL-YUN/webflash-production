package com.sql.dao.bean;

public class Msql {

	private String table; // 表名
	private String tables; // 关联表
	private String field; // 可查字段
	private String where; // 条件
	private String order; // 排序
	private int action=0; // 开始标签
	private int end=8; // 结束标签
	private int id; // id查询

	private boolean cfalger; // 求总数


	public String getTables() {
		return tables;
	}

	/**
	 * 关联表设置
	 * 
	 * @param tables
	 */
	public void setTables(String tables) {
		this.tables = tables;
	}


	public String getTable() {
		return table;
	}

	/**
	 * 设置表名
	 * 
	 * @param table
	 */
	public void setTable(String table) {
		this.table = table;
	}

	public String getField() {
		return field;
	}

	/**
	 * 可查字段
	 * 
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}

	public String getWhere() {
		return where;
	}

	/**
	 * 设置条件
	 * 
	 * @param where
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isCfalger() {
		return cfalger;
	}

	public void setCfalger(boolean cfalger) {
		this.cfalger = cfalger;
	}

	public int getAction() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}
