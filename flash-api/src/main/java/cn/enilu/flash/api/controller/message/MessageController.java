package cn.enilu.flash.api.controller.message;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.message.Message;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.message.MessageService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.HttpUtil;
import cn.enilu.flash.utils.JsonUtil;
import cn.enilu.flash.utils.Maps;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.vode.bean.entity.VodeBean;
import cn.enilu.vode.service.VodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.Security;
import java.util.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private VodeService vodeService;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.MSG})
    public Object list(@RequestParam(required = false) String tplCode,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate) {
        Page<Message> page = new PageFactory<Message>().defaultPage();
        page.addFilter("tplCode", tplCode);
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
        page = messageService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @DeleteMapping
    @BussinessLog(value = "清空所有历史消息")
    @RequiresPermissions(value = {Permission.MSG_CLEAR})
    public Object clear() {
        messageService.clear();
        return Rets.success();
    }

    @PostMapping("/testSender")
    @BussinessLog(value = "发送测试短信")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object testSend(@RequestParam String tplCode, @RequestParam String receiver, @RequestParam String params) {
        LinkedHashMap map = JsonUtil.fromJson(LinkedHashMap.class, params);
        messageService.sendSms(tplCode, receiver, map);
        return Rets.success();
    }



    @PostMapping("/sendYj")
    @BussinessLog(value = "发送邮件")
    public Object sendYj(@RequestParam("emil") String emile) {
        //LinkedHashMap map = JsonUtil.fromJson(LinkedHashMap.class, params);
        //发送简单邮件
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;

        messageService.sendSimpleEmail("EMAIL_TEST", "3354627304@qq.com",
                emile, null, "verification code", "", "验证码"+String.valueOf( randomNumber));

            //发送html模板复杂邮件
        /**
        messageService.sendTplEmail("EMAIL_HTML_TEMPLATE_TEST", "fromAccount@qq.com",
                "toAccount@qq.com", null, "html模板邮件", Maps.newHashMap("userName", "李四", "appName", "WEB-FLASH"));
           */
        //发送带附件的邮件
        /**
        messageService.sendTplEmail("EMAIL_HTML_TEMPLATE_TEST", "fromAccount@qq.com",
                "toAccount@qq.com", null, "带附件的邮件",
                "测试附件.txt",new FileSystemResource(new File("d:\\test.txt")),
                Maps.newHashMap("userName", "李四", "appName", "WEB-FLASH"));
         **/
        //messageService.sendSms(tplCode, receiver, map);
        return Rets.success(String.valueOf( randomNumber));
    }
    @PostMapping("/sendVoid")
    @BussinessLog(value = "发送邮件")
    public Object sendVoid(@RequestParam("emil") String emile,@RequestParam("fdUrl") String fdUrl) {
        //LinkedHashMap map = JsonUtil.fromJson(LinkedHashMap.class, params);
        //发送简单邮件
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;

        List<VodeBean> list= vodeService.queryAll();
        List<String> data=new ArrayList<>();

        for(VodeBean vodeBean: list){
            data.add("http://localhost:9528"+fdUrl+"/file/download?idFile="+vodeBean.getVideId()+"\r\n");
        }


        messageService.sendSimpleEmail("EMAIL_TEST_SENDVOID", "3354627304@qq.com",
                emile, null, "Download", "", data.toString());


        return Rets.success(String.valueOf( randomNumber));
    }
}