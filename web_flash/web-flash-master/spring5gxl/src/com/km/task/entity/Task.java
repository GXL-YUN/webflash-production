package com.km.task.entity;

import com.sql.dao.bean.Model;

public class Task extends Model{
	
	public String  fd_name;
	
	public String fd_class;

	public String  fd_massage;
	
	public String  fd_url;
	
	public String  fd_time;
	
	public String getFd_time() {
		return fd_time;
	}

	public void setFd_time(String fd_time) {
		this.fd_time = fd_time;
	}

	public String  fd_groupa;
	
	public String  fd_groupb;
	
	public String  fd_groupc;

	public String getFd_url() {
		return fd_url;
	}

	public void setFd_url(String fd_url) {
		this.fd_url = fd_url;
	}

	public String getFd_groupa() {
		return fd_groupa;
	}

	public void setFd_groupa(String fd_groupa) {
		this.fd_groupa = fd_groupa;
	}

	public String getFd_groupb() {
		return fd_groupb;
	}

	public void setFd_groupb(String fd_groupb) {
		this.fd_groupb = fd_groupb;
	}



	public String getFd_groupc() {
		return fd_groupc;
	}

	public void setFd_groupc(String fd_groupc) {
		this.fd_groupc = fd_groupc;
	}

	public String getFd_name() {
		return fd_name;
	}

	public void setFd_name(String fd_name) {
		this.fd_name = fd_name;
	}

	public String getFd_class() {
		return fd_class;
	}

	public void setFd_class(String fd_class) {
		this.fd_class = fd_class;
	}

	public String getFd_massage() {
		return fd_massage;
	}

	public void setFd_massage(String fd_massage) {
		this.fd_massage = fd_massage;
	}
	
	

}
