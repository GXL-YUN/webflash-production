package cn.enilu.project.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.project.bean.model.ProjectModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao  extends BaseRepository<ProjectModel, String> {
}
