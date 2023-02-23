package com.sql.dao.service.icmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sql.dao.bean.OneTable;
import com.sql.dao.mapper.DelectMapper;
import com.sql.dao.mapper.FindMapper;
import com.sql.dao.service.DelectService;
@Service
public class DelectServiceicmp implements DelectService{

	

	@Autowired
	private DelectMapper delectMapper;
	@Override
	public String delectByID(OneTable obj) {
		
		Integer delectByID = delectMapper.delectByID(obj);
		// TODO Auto-generated method stub
		return delectByID.toString();
	}

}
