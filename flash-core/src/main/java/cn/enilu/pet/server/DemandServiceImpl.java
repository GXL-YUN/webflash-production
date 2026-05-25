package cn.enilu.pet.server;

import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Demand;
import cn.enilu.pet.dao.DemandDao;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandServiceImpl extends BaseService<Demand, String, DemandDao> {
    @Autowired
    private DemandDao DemandDao;

}
