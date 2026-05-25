package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Addresses;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.dao.AddressesDao;
import cn.enilu.pet.dao.AdoptDao;
import cn.enilu.pet.server.AddressesServiceImpl;
import cn.enilu.pet.server.AdoptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/pet/addresses")
public class AddressesController  extends
        CrudController<Addresses, String, AddressesDao, RequertInfo> {



    @Autowired
    private AddressesServiceImpl addressesServiceImpl;

    @Override
    protected BaseService<Addresses, String, AddressesDao> getService() {
        return addressesServiceImpl;
    }




}
