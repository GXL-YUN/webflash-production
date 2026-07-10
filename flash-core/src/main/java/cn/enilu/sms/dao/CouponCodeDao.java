package cn.enilu.sms.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.sms.bean.CouponCode;
import org.springframework.stereotype.Repository;


@Repository
public interface CouponCodeDao  extends BaseRepository<CouponCode, String> {
}