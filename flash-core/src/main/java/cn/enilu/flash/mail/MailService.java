package cn.enilu.flash.mail;


import org.springframework.stereotype.Repository;

@Repository
public interface MailService {

    void sendText(String to, String subject, String content);

    void sendHtml(String to, String subject, String htmlContent);

    void sendWithAttachment(
            String to,
            String subject,
            String content,
            String attachmentPath
    );

    void sendGroup(
            String[] to,
            String[] cc,
            String[] bcc,
            String subject,
            String content,
            boolean html
    );
}


/**
 *
 *
 *
 * @Autowired
 * private MailService mailService;
 *
 * // 1. 文本
 * mailService.sendText(
 *     "test@example.com",
 *     "测试标题",
 *     "这是一封测试邮件"
 * );
 *
 * // 2. HTML
 * mailService.sendHtml(
 *     "test@example.com",
 *     "HTML 邮件",
 *     "<h1>Hello</h1><p>这是 HTML 邮件</p>"
 * );
 *
 * // 3. 附件
 * mailService.sendWithAttachment(
 *     "test@example.com",
 *     "带附件",
 *     "请查收附件",
 *     "/tmp/demo.pdf"
 * );
 *
 * // 4. 群发
 * mailService.sendGroup(
 *     new String[]{"a@qq.com", "b@qq.com"},
 *     new String[]{"cc@qq.com"},
 *     null,
 *     "群发测试",
 *     "大家好",
 *     false
 * );
 */