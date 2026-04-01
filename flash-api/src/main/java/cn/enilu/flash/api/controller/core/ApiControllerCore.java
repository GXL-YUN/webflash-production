package cn.enilu.flash.api.controller.core;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.dao.BaseRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;


@Slf4j
public abstract class ApiControllerCore<T, ID extends Serializable, R extends BaseRepository<T, ID>> {

    @Autowired
    private R dao;



    @PostMapping(value = "/list")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object list(@RequestBody RequertInfo requertInfo) {
        try {
            Page<ProjectModel> page = new PageFactory<ProjectModel>().defaultPage();

            page.setSize(requertInfo.getSize());
            page.setCurrent(requertInfo.getCurrent());
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
           // page = dao.queryPage(page);
           // dao.
           // page.getRecords();
            //List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
           // return Rets.success(page);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
        return null;
    }




}
