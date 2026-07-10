package cn.enilu.pet.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.pet.bean.model.Addresses;
import cn.enilu.pet.bean.model.Adopt;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesDao extends BaseRepository<Addresses, String> {
}
