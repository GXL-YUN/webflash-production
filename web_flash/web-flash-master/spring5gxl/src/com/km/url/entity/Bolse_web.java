package com.km.url.entity;

import com.sql.dao.bean.Model;
import com.sql.dao.bean.OneTable;
import com.sun.org.apache.xpath.internal.operations.Mod;

public class Bolse_web extends Model {


	    private String fd_name;   //url路径名称

	    private String fd_url;    //url

	    private String fd_massage;   //url信息


	   
	  
		public String getFd_name(){

	        return this.fd_name;
	    }
	    public void setFd_name(String fd_name){

	        this.fd_name = fd_name;
	    }
	    public String getFd_url(){

	        return this.fd_url;
	    }
	    public void setFd_url(String fd_url){

	        this.fd_url = fd_url;
	    }
	    public String getFd_massage(){

	        return this.fd_massage;
	    }
	    public void setFd_massage(String fd_massage){

	        this.fd_massage = fd_massage;
	    }
	}


