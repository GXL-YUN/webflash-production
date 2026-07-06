package cn.enilu.pet.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.bean.model.Payment_record;
import org.springframework.stereotype.Repository;


@Repository
public interface Payment_recordDao extends BaseRepository<Payment_record, String> {
}
