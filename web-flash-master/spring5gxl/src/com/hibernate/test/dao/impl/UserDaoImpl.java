package com.hibernate.test.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

import com.hibernate.test.dao.UserDao;
import com.hibernate.test.model.adminstrator;


@Service
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {
	

	 @Resource(name="sessionFactory")
	public boolean login(adminstrator admin) {
      List<adminstrator> alist;	  
      alist=(List<adminstrator>) getHibernateTemplate().find("from adminstrator");
	  if(alist.size()>0){
		  System.out.println("good!find it!");
		  return true;
	  }else{
		  System.out.println("sorry can not find it!");
		  return false;
	  }		 
	}

     
}
