package cn.enilu.pet.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.bean.model.PetsList;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsListDao extends BaseRepository<PetsList, String> {
}
