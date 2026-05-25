package cn.enilu.flash.api.controller.test;

import cn.enilu.flash.mail.MailService;
import cn.enilu.flash.service.system.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试工具
 * 启动api服务后，通过swagger地址：http://localhost:8082/swagger-ui.html#!/test-controller/testUsingGET 来做一些测试
 *
 * @Author enilu
 * @Date 2021/6/2 15:23
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;


    @Autowired
    private MailService mailService;


    @GetMapping("sendText")
    public void sendText(@RequestParam("key") String key) {
        // 1. 文本
        mailService.sendText("13720661531@163.com", "测试标题", "这是一封测试邮件");

    }

    @GetMapping("sendHtml")
    public void sendHtml(@RequestParam("key") String key) {
        //2. HTML
        mailService.sendHtml("13720661531@163.com", "HTML 邮件", "<h1>Hello</h1><p>这是 HTML 邮件</p>");
    }


    @GetMapping("sendWithAttachment")
    public void sendWithAttachment(@RequestParam("key") String key) {
          // 3. 附件
          mailService.sendWithAttachment("13720661531@163.com", "带附件", "请查收附件", "/tmp/demo.pdf");
    }
    @GetMapping("sendGroup")
    public void sendGroup(@RequestParam("key") String key) {
        // 4. 群发        mailUtil.sendHtml("guxinlei@lsyxtech.com","cesh","<br>sajhwqiohi");
        mailService.sendGroup(new String[]{"13720661531@163.com", "guxinlei@lsyxtech.com"},new String[]{"cc@qq.com"},null,"群发测试","大家好",false);
    }

    @GetMapping
    public void test(@RequestParam("key") String key) {
        if ("lock".equals(key)) {
            testService.lock();
        }
        if (key.startsWith("trans")) {
            if (key.contains("yes")) {
                testService.userTrans();
            }
            if (key.contains("no")) {
                testService.noTrans();
            }

        }
    }
}
