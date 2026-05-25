package cn.enilu.flash.api.controller;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.BaseEntity;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.sys.att.service.imp.SysAttMainService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.pet.bean.model.Adopt;
import cn.enilu.pet.bean.model.Demand;
import cn.enilu.pet.em.DemandStatus;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.sms.service.BizException;
import cn.enilu.util.email.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 *路由基础配置工具类
 * @param <T>
 * @param <ID>
 * @param <DTO>
 * @param <Q>
 */

@Slf4j
@RestController
public abstract class CrudController<T, ID, DTO,  Q extends RequertInfo> extends BaseController {
    protected abstract <T extends BaseService> T getService();

    @Autowired
    private SysAttMainService sysAttMainService;

    //获取对象详细信息
    @GetMapping("/view/{id}")
    public Object getById(@PathVariable String id) {
        return Rets.success(getService().getById(id));
    }

    //获取列表
    @PostMapping("/get/list")
    public Object list(@RequestBody RequertInfo requertInfo) {
        try {
            Page<?> page = new PageFactory<ProjectModel>().defaultPage();
            page.setSize(requertInfo.getSize());
            page.setCurrent(requertInfo.getCurrent());
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            page = getService().queryPage(page);
            page.getRecords();
            //List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
            return Rets.success(page);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }




    /**
     * 查询列表
     * @param user_id
     * @return
     */
    @GetMapping("/List")
    public Object geList(@RequestParam(required = false) String user_id) {
        try {
            Page<T> page = new PageFactory<T>().defaultPage();
            page.addFilter("fdUserId", SearchFilter.Operator.EQ, user_id, SearchFilter.Join.and);
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            Page<T> date= getService().queryPage(page);
            MailUtils emin=new MailUtils();
            //emin.sendMail("13720661531@163.com","测试","cehsi hsu ",false);
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
    @PostMapping("/add")
    public Object create(@Valid @RequestBody T  date) throws BizException {
        getService().insert(date);
        return Rets.success("创建成功");
    }

    @PutMapping("/update/{id}")
    public Object update(@Valid @RequestBody T dto) {
        getService().update(dto);
        return Rets.success("更新成功");
    }

    @DeleteMapping("delect/{id}")
    public Object delete(@PathVariable String id) {
        getService().delete(id);
        return Rets.success("删除成功");
    }
}