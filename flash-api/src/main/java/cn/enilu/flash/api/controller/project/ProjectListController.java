package cn.enilu.flash.api.controller.project;

import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.project.bean.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import cn.enilu.project.service.ProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            List<ProjectModel> list=projectServiceImpl.queryAll();
            return Rets.success(list);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }

    //项目查询
    @PostMapping(value = "/add")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object add(@RequestBody ProjectModel date) {
        try {
            projectServiceImpl.insert(date);
            //List<ProjectModel> list=projectServiceImpl.queryAll();
            return Rets.success("成功");
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("新增列表异常");
        }
    }


}
