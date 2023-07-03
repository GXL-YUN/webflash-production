package cn.enilu.kmss.service;

import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.dao.cms.ArticleRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.kmss.dao.RecordDao;
import cn.enilu.kmss.bean.entity.RecordBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 档案管理基础类
 */
@Service
public class RecordService extends BaseService<RecordBean, Long, RecordDao> {
    @Autowired
     private RecordDao recordDao;
    /**
     * 查询全部数据
     */
    public List<RecordBean> queryIndexNews() {
       List<RecordBean> bannerList = recordDao.findAll();
        return bannerList;
    }
}
