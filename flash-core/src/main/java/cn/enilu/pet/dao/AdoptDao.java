package cn.enilu.pet.dao;

import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.bean.model.Demand;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptDao  extends BaseRepository<Adopt, String> {

}
