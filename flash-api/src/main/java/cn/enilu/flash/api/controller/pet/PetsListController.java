package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Addresses;
import cn.enilu.pet.bean.model.PetsList;
import cn.enilu.pet.dao.AddressesDao;
import cn.enilu.pet.dao.PetsListDao;
import cn.enilu.pet.server.AddressesServiceImpl;
import cn.enilu.pet.server.PetsListServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/pet/pets")
public class PetsListController  extends
        CrudController<PetsList, String, PetsListDao, RequertInfo> {


    @Autowired
    private PetsListServiceImpl petsListServiceImpl;

    @Override
    protected BaseService<PetsList, String, PetsListDao> getService() {
        return petsListServiceImpl;
    }


}
