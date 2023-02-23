package com.km.doument.entity;


import java.lang.reflect.Field;
import java.sql.Date;

import com.sql.dao.bean.Model;


public class Blogs_document extends Model{

    private String fd_name;
    private String fd_subject;
    private String fd_content;
    private String fd_massage;

	public String getFd_name() {
		return fd_name;
	}

	public void setFd_name(String fd_name) {
		this.fd_name = fd_name;
	}

	public String getFd_subject() {
		return fd_subject;
	}

	public void setFd_subject(String fd_subject) {
		this.fd_subject = fd_subject;
	}

	public String getFd_content() {
		return fd_content;
	}

	public void setFd_content(String fd_content) {
		this.fd_content = fd_content;
	}

	public String getFd_massage() {
		return fd_massage;
	}

	public void setFd_massage(String fd_massage) {
		this.fd_massage = fd_massage;
	}


   
}
