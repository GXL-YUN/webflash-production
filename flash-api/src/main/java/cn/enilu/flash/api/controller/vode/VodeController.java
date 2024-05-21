package cn.enilu.flash.api.controller.vode;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.vode.bean.entity.VodeBean;
import cn.enilu.vode.service.VodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vode")
public class VodeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(VodeController.class);
    @Autowired
    private VodeService vodeService;


    @PostMapping(value = "save.do")
    @BussinessLog(value = "新增视频", key = "name")
    @RequiresPermissions(value = {Permission.ARTICLE_EDIT})
    public Object save() {
        VodeBean article = getFromJson(VodeBean.class);
        if (article.getId() != null) {
            VodeBean old = vodeService.get(article.getId());

            vodeService.update(old);
        } else {
            vodeService.insert(article);
        }
        return Rets.success();
    }

    @GetMapping(value = "/list.do")
    public Object list(@RequestParam(required = false) String title,
                       @RequestParam(required = false) String author,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate
    ) {
        Page<VodeBean> page = new PageFactory<VodeBean>().defaultPage();
        page.addFilter("title", SearchFilter.Operator.LIKE, title);
        page.addFilter("author", SearchFilter.Operator.EQ, author);
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
        page = vodeService.queryPage(page);
        return Rets.success(page);
    }
    @DeleteMapping
    @PostMapping(value = "del.do")
    @BussinessLog(value = "删除视频", key = "id")
    @RequiresPermissions(value = {Permission.ARTICLE_DEL})
    public Object remove(Long id) {
        vodeService.delete(id);
        return Rets.success();
    }

    @GetMapping(value = "getById.do")
    @BussinessLog(value = "查看视频详细", key = "id")
    @RequiresPermissions(value = {Permission.ARTICLE})
    public Object get(@Param("id") Long id) {
        VodeBean article = vodeService.get(id);
        return Rets.success(article);
    }
}
