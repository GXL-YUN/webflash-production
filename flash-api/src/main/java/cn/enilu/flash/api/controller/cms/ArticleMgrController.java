package cn.enilu.flash.api.controller.cms;

import cn.enilu.flash.api.ApiApplication;
import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.cms.ArticleService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.GetAddBrowser;
import cn.enilu.flash.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章管理
 */
@RestController
@RequestMapping("/article")
public class ArticleMgrController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ArticleMgrController.class);
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @BussinessLog(value = "编辑文章", key = "name")
    @RequiresPermissions(value = {Permission.ARTICLE_EDIT})
    public Object save() {
        logger.info("ssss");
        Article article = getFromJson(Article.class);
        if (article.getId() != null) {
            Article old = articleService.get(article.getId());
            old.setAuthor(article.getAuthor());
            old.setContent(article.getContent());
            old.setIdChannel(article.getIdChannel());
            old.setImg(article.getImg());
            old.setTitle(article.getTitle());
            articleService.update(old);
        } else {
            articleService.insert(article);
        }
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除文章", key = "id")
    @RequiresPermissions(value = {Permission.ARTICLE_DEL})
    public Object remove(Long id) {
        articleService.delete(id);
        return Rets.success();
    }

    @GetMapping
    @RequiresPermissions(value = {Permission.ARTICLE})
    public Object get(@Param("id") Long id) {
        Article article = articleService.get(id);
        return Rets.success(article);
    }

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.ARTICLE})
    public Object list(@RequestParam(required = false) String title,
                       @RequestParam(required = false) String author,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate
    ) {
        Page<Article> page = new PageFactory<Article>().defaultPage();
        page.addFilter("title", SearchFilter.Operator.LIKE, title);
        page.addFilter("author", SearchFilter.Operator.EQ, author);
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
        //List<Article> ss= articleService.queryAll();
        page = articleService.queryPage(page);
        return Rets.success(page);
    }
}
