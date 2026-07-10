package cn.enilu.pet.server;


import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Payment_record;
import cn.enilu.pet.dao.Payment_recordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付记录
 */
@Service
public class Payment_recordServiceImpl extends BaseService<Payment_record, String, Payment_recordDao> {
    @Autowired
    private Payment_recordDao DemandDao;
}
