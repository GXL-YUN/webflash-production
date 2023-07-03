package cn.enilu.flash.api.controller.record;


import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.cms.Channel;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.kmss.bean.entity.RecordBean;
import cn.enilu.kmss.service.RecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/record")
public class ListRecordController {

   @Autowired
    private RecordService recordService;
    /**
     * 档案管理数据展示
     */
    @GetMapping("/list")
    public Object list() {
        List<RecordBean> list= recordService.queryIndexNews();
        System.out.println("sss");
        return Rets.success(list);
        //return Rets.success(null);
    }
    /**
     * 档案管理数据新增修改
     * @return
     */
    @PostMapping
    @BussinessLog(value = "编辑栏目", key = "name")
    public Object Add(@RequestBody @Valid RecordBean recordDao) {
        if (recordDao.getId() == null) {
            recordService.insert(recordDao);
        } else {
            recordService.update(recordDao);
        }
        return Rets.success();
    }
    /**
     * 档案管理数据删除
     * @return
     */
    @DeleteMapping
    @BussinessLog(value = "删除档案", key = "id")
    public Object remove(Long id) {
        recordService.delete(id);
        return Rets.success();
    }
}
