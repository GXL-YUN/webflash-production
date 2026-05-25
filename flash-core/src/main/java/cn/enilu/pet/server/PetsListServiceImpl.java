package cn.enilu.pet.server;

import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Addresses;
import cn.enilu.pet.bean.model.PetsList;
import cn.enilu.pet.dao.AddressesDao;
import cn.enilu.pet.dao.PetsListDao;
import org.springframework.stereotype.Service;

@Service
public class PetsListServiceImpl extends BaseService<PetsList, String, PetsListDao> {
}
