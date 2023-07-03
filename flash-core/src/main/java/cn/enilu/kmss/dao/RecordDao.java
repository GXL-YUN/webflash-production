package cn.enilu.kmss.dao;

import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.kmss.bean.entity.RecordBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

public interface RecordDao extends BaseRepository<RecordBean, Long> {

    /**
     * 查询所有档案信息
     */
   // List<Article> findAllByIdChannel();
}
