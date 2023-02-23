package com.hibernate.test.model;

/**
 * comment entity. @author MyEclipse Persistence Tools
 */

public class comment implements java.io.Serializable {

	// Fields

	private Integer commentId;
	private blog blog;
	private String commentName;

	// Constructors

	/** default constructor */
	public comment() {
	}

	/** full constructor */
	public comment(blog blog, String commentName) {
		this.blog = blog;
		this.commentName = commentName;
	}

	// Property accessors

	public Integer getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public blog getBlog() {
		return this.blog;
	}

	public void setBlog(blog blog) {
		this.blog = blog;
	}

	public String getCommentName() {
		return this.commentName;
	}

	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}

}