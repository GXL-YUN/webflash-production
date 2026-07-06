package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.dao.AdoptDao;
import cn.enilu.pet.em.ADoptStatus;
import cn.enilu.pet.server.AdoptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/pet/adopt")
public class AdoptController extends
        CrudController<Adopt, String, AdoptDao, RequertInfo> {


    @Autowired
    private AdoptServiceImpl adoptServiceImpl;
    @Override
    protected BaseService<Adopt, String, AdoptDao> getService() {
        return adoptServiceImpl;
    }


    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     * @param user_id
     * @param status
     * @return
     */
    @GetMapping("/getList")
    public Object geList(@RequestParam(required = false) String user_id, @RequestParam(required = false) String status, @RequestParam(required = false) String typestatus) {
        try {
            Page<Adopt> page = new PageFactory<Adopt>().defaultPage();
            if(StringUtil.isNotEmpty(status)&&!"all".equals(status)){
                page.addFilter("fdStatus", SearchFilter.Operator.EQ, status, SearchFilter.Join.and);
            }

            if(StringUtil.isNotEmpty(typestatus)&&!"all".equals(typestatus)){
                page.addFilter("fdPetType", SearchFilter.Operator.EQ, typestatus, SearchFilter.Join.and);
            }

            //page.addFilter("fdUserId", SearchFilter.Operator.EQ, user_id, SearchFilter.Join.and);
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            Page<Adopt> date= getService().queryPage(page);
            return Rets.success(date.getRecords());
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }

    /**
     * 新增
     * @param date
     * @return
     */
    @PostMapping("/addAdopt")
    public Object create(@Valid @RequestBody Adopt  date) {
        Adopt main=getService().insert(date);
        try{
            fileService.getListByIdFileChangeMainId(main.getFdId(),date.getFile());
            log.info("附件更新完成");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Rets.success( main.getFdId());
    }

    /**
     * 变更为已领养
     * @param id
     * @return
     */
    @GetMapping("/setConfirmAdoption/{id}")
    public Object setConfirmAdoption(@PathVariable String id) {
        Adopt main=getService().get(id);
        main.setFdStatus(ADoptStatus.ADOPTED.getCode());

        getService().update(main);
        //是否需要生成宠物档案
        return Rets.success(main.getFdId());
    }


}
