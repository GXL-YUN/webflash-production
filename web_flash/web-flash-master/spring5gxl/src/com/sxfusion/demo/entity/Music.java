package com.sxfusion.demo.entity;

import com.sql.dao.bean.Model;

/**
 * 音乐抓取类
 * @author gxl
 *
 */
public class Music  extends Model{
	
	private String fd_name;
	private String fd_url;
	private String fd_picurl;
	private String fd_artistsname;
	

	public String getFd_name() {
		return fd_name;
	}
	public void setFd_name(String fd_name) {
		this.fd_name = fd_name;
	}
	public String getFd_url() {
		return fd_url;
	}
	public void setFd_url(String fd_url) {
		this.fd_url = fd_url;
	}
	public String getFd_picurl() {
		return fd_picurl;
	}
	public void setFd_picurl(String fd_picurl) {
		this.fd_picurl = fd_picurl;
	}
	public String getFd_artistsname() {
		return fd_artistsname;
	}
	public void setFd_artistsname(String fd_artistsname) {
		this.fd_artistsname = fd_artistsname;
	}

	public String getFd_start() {
		
		if(fd_start==null) {
			fd_start="0";
		}
		return fd_start;
	}

	public void setFd_start(String fd_start) {
		this.fd_start = fd_start;
	}

	
	
}
