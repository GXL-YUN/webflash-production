package cn.enilu.pet.server;

import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.dao.AdoptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdoptServiceImpl  extends BaseService<Adopt, String, AdoptDao> {
    @Autowired
    private AdoptDao adoptDao;

}
