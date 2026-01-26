package cn.enilu.project.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.kmss.bean.entity.RecordBean;
import cn.enilu.kmss.dao.RecordDao;
import cn.enilu.project.bean.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl  extends BaseService<ProjectModel, Long, ProjectDao> {
    @Autowired
    private ProjectDao projectDao;

}
