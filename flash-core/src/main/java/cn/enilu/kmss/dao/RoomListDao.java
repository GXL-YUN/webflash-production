package cn.enilu.kmss.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.kmss.bean.entity.RecordBean;
import cn.enilu.kmss.bean.entity.RoomList;

public interface RoomListDao extends BaseRepository<RoomList, Long> {

    /**
     * 查询所有档案信息
     */
    // List<Article> findAllByIdChannel();
}
