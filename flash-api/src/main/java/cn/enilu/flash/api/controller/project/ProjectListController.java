package cn.enilu.flash.api.controller.project;

import cn.enilu.Thread.ThreadPoolUtil;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.sys.att.service.imp.SysAttMainService;
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
public class ProjectListController  {

    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    @Autowired
    private FileService fileService;


    @Autowired
    private SysAttMainService sysAttMainService;


    //项目查询
    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    @PostMapping(value = "/list")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object list(@RequestBody RequertInfo requertInfo) throws Exception{
        try {
            Page<ProjectModel> page = new PageFactory<ProjectModel>().defaultPage();

            page.setSize(requertInfo.getSize());
            page.setCurrent(requertInfo.getCurrent());
            Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
            page.setSort(sort);
            page = projectServiceImpl.queryPage(page);
            page.getRecords();
            //List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
            return Rets.success(page);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }

    private void processItem(String item) {
        // 业务处理逻辑
        log.info("测试"+item);
    }
    /**
     * 查询项目详情
     * @param fdId   项目id
     * @return
     */
    @PostMapping(value = "/view")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object list(@RequestParam("fdId") String fdId) {
        try {
            //List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
            ProjectModel model=projectServiceImpl.getById(fdId);
            return Rets.success(model);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }
    /**
     * 更新项目
     * @param date
     * @return
     */
    @PostMapping(value = "/update")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object update(@RequestBody ProjectModel date) {
        try {
            //List<ProjectModel> list= projectServiceImpl.queryAll((List<SearchFilter>) null,sort);
            ProjectModel model=projectServiceImpl.update(date);
            return Rets.success(model);
        } catch (Exception e) {
            log.error("查询全部异常", e);
            return Rets.failure("查询列表异常");
        }
    }

    /**
     * 风险点查询
     * @param date
     * @return
     */


    /**
     * 代办项查询
     * @param date
     * @return
     */


    /**
     * 风险点新增
     * @param date
     * @return
     */


    /**
     * 代办项新增
     * @param date
     * @return
     */


    /**
     * 功能点新查询
     * @param date
     * @return
     */

    /**
     * 功能点新增
     * @param date
     * @return
     */


    /**
     * 项目新增(区分是需求还是项目)
     * @param date
     * @return
     */

    /**
     * 更新项目（需求）进度
     * @param date
     * @return
     */


    /**
     *
     * @param date
     * @return
     */






    @PostMapping(value = "/add")
    // @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object add(@RequestBody ProjectModel date) {
        ProjectModel model=null;
        try {
             model= projectServiceImpl.insert(date);
            //保存附件数据

            try{
                sysAttMainService.addSysAttMain(date.getAttacherList(),model.getClass().getName(),model.getFdId());
            }catch (Exception e){
                log.error("附件新增列表异常", e);
            }

            return Rets.success("成功");
        } catch (Exception e) {
            log.error("新增单条异常", e);
            return Rets.failure("新增单条异常");
        }
    }
}
