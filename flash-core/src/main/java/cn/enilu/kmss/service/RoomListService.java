package cn.enilu.kmss.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.kmss.bean.entity.RoomList;
import cn.enilu.kmss.dao.RoomListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class RoomListService extends BaseService<RoomList, Long, RoomListDao>{
        @Autowired
        private RoomListDao roomListDao;

        /**
         * 查询全部数据
         */
        public List<RoomList> queryIndexNews() {
        List<RoomList> bannerList = roomListDao.findAll();
        return bannerList;
    }
    }
