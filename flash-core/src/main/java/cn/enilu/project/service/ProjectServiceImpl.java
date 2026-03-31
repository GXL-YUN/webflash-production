package cn.enilu.project.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl  extends BaseService<ProjectModel, String, ProjectDao> {
    @Autowired
    private ProjectDao projectDao;

}
