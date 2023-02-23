package com.sql.dao.service.icmp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sql.dao.mapper.InsertDateMapper;
import com.sql.dao.service.InsertService;

@Service
public class InsertIntoModelService implements InsertService {
	
	/**
	 * 新增方法
	 *  参数
	 */
	@Autowired
	private InsertDateMapper i;

	@Override
	public boolean insert(Map map) {
		// TODO Auto-generated method stub
		
		boolean insert = i.insert(map);
		
		return insert;
	}

}
