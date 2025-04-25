package cn.enilu.kmss.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.kmss.bean.entity.AnnouncementBean;
import cn.enilu.kmss.bean.entity.RecordBean;
import cn.enilu.kmss.dao.AnnouncementDao;
import cn.enilu.kmss.dao.RecordDao;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService extends BaseService<AnnouncementBean, Long, AnnouncementDao> {
}
