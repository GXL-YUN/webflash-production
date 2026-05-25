package cn.enilu.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine; // 用于模板邮件

    /**
     * 发送简单邮件
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@163.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     */
    public void sendHtmlMail(String to, String subject, String htmlContent)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("your_email@163.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    /**
     * 发送模板邮件
     */
    public void sendTemplateMail(String to, String subject, String templateName,
                                 Context context) throws MessagingException {
        String emailContent = templateEngine.process(templateName, context);
        sendHtmlMail(to, subject, emailContent);
    }

    /**
     * 发送带附件的邮件
     */
    public void sendAttachmentMail(String to, String subject, String content,
                                   File file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("your_email@163.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content);

        helper.addAttachment(file.getName(), file);

        mailSender.send(message);
    }
}