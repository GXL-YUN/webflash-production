package cn.enilu.project.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.kmss.bean.entity.RecordBean;
import cn.enilu.project.bean.ProjectModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao  extends BaseRepository<ProjectModel, Long> {
}
