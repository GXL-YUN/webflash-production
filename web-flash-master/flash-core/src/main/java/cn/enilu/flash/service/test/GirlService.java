package cn.enilu.flash.service.test;


import cn.enilu.flash.bean.entity.test.Girl;
import cn.enilu.flash.dao.test.GirlRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GirlService extends BaseService<Girl,Long,GirlRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GirlRepository girlRepository;

}

