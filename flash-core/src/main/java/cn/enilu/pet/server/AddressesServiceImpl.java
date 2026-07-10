package cn.enilu.pet.server;

import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Addresses;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.dao.AddressesDao;
import cn.enilu.pet.dao.AdoptDao;
import org.springframework.stereotype.Service;

/**
 * 地址本
 */
@Service
public class AddressesServiceImpl  extends BaseService<Addresses, String, AddressesDao> {
}
