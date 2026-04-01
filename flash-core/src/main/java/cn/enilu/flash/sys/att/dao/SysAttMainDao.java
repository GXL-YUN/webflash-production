package cn.enilu.flash.sys.att.dao;

import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.sys.att.bean.SysAttMain;
import cn.enilu.flash.dao.BaseRepository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public abstract interface SysAttMainDao extends BaseRepository<SysAttMain, String>  {

}
