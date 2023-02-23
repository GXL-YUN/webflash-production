package com.hibernate.test.model;

import java.util.HashSet;
import java.util.Set;

/**
 * category entity. @author MyEclipse Persistence Tools
 */

public class category implements java.io.Serializable {

	// Fields

	private Integer categoryId;
	private String categoryName;
	private Set blogs = new HashSet(0);

	// Constructors

	/** default constructor */
	public category() {
	}

	/** minimal constructor */
	public category(String categoryName) {
		this.categoryName = categoryName;
	}

	/** full constructor */
	public category(String categoryName, Set blogs) {
		this.categoryName = categoryName;
		this.blogs = blogs;
	}

	// Property accessors

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Set getBlogs() {
		return this.blogs;
	}

	public void setBlogs(Set blogs) {
		this.blogs = blogs;
	}

}