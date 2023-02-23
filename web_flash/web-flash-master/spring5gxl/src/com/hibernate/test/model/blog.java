package com.hibernate.test.model;

import java.util.HashSet;
import java.util.Set;

/**
 * blog entity. @author MyEclipse Persistence Tools
 */

public class blog implements java.io.Serializable {

	// Fields

	private Integer blogId;
	private category category;
	private String blogTitle;
	private String blogContent;
	private String blogTime;
	private Set comments = new HashSet(0);

	// Constructors

	/** default constructor */
	public blog() {
	}

	/** minimal constructor */
	public blog(category category, String blogTitle, String blogContent,
			String blogTime) {
		this.category = category;
		this.blogTitle = blogTitle;
		this.blogContent = blogContent;
		this.blogTime = blogTime;
	}

	/** full constructor */
	public blog(category category, String blogTitle, String blogContent,
			String blogTime, Set comments) {
		this.category = category;
		this.blogTitle = blogTitle;
		this.blogContent = blogContent;
		this.blogTime = blogTime;
		this.comments = comments;
	}

	// Property accessors

	public Integer getBlogId() {
		return this.blogId;
	}

	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}

	public category getCategory() {
		return this.category;
	}

	public void setCategory(category category) {
		this.category = category;
	}

	public String getBlogTitle() {
		return this.blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}

	public String getBlogContent() {
		return this.blogContent;
	}

	public void setBlogContent(String blogContent) {
		this.blogContent = blogContent;
	}

	public String getBlogTime() {
		return this.blogTime;
	}

	public void setBlogTime(String blogTime) {
		this.blogTime = blogTime;
	}

	public Set getComments() {
		return this.comments;
	}

	public void setComments(Set comments) {
		this.comments = comments;
	}

}