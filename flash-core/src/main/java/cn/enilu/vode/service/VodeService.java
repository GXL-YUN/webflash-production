package cn.enilu.vode.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.vode.dao.VodeDao;
import cn.enilu.vode.bean.entity.VodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档案管理基础类
 */
@Service
public class VodeService extends BaseService<VodeBean, Long, VodeDao> {
    @Autowired
    private VodeDao vodeDao;

    /**
     * 查询全部数据
     */
    public List<VodeBean> queryIndexNews() {
        List<VodeBean> bannerList = vodeDao.findAll();
        return bannerList;
    }
}
