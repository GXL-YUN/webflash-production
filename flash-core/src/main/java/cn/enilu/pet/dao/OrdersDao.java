package cn.enilu.pet.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.bean.model.Orders;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersDao extends BaseRepository<Orders, String> {
}
