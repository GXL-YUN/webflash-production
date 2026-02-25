package cn.enilu.flash.api.controller.project;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.service.ProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectListController {

    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    //项目查询
    @PostMapping(value = "/list")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object list() {
        try {
            Page<ProjectModel> page = new PageFactory<ProjectModel>().defaultPage();
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            page = projectServiceImpl.queryPage(page);
            page.getRecords();
            List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
            return Rets.success(list);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }


    @Autowired
    private FileService fileService;
    //项目查询
    @PostMapping(value = "/add")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object add(@RequestBody ProjectModel date) {
        try {
            projectServiceImpl.insert(date);
            //fileService.updateList(date.geta);

            //修改附件
            //List<ProjectModel> list=projectServiceImpl.queryAll();
            return Rets.success("成功");
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("新增列表异常");
        }
    }


}
