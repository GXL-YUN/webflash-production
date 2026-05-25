package cn.enilu.flash.mail.impl;

import cn.enilu.flash.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 文本邮件
     */
    @Override
    @Async//异步发送（防止接口卡顿）
    public void sendText(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * HTML 邮件
     */
    @Override
    @Async//异步发送（防止接口卡顿）
    public void sendHtml(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送 HTML 邮件失败", e);
        }
    }

    /**
     * 带附件邮件
     */
    @Override
    @Async//异步发送（防止接口卡顿）
    public void sendWithAttachment(
            String to,
            String subject,
            String content,
            String attachmentPath) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.addAttachment("附件", new File(attachmentPath));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送附件邮件失败", e);
        }
    }

    /**
     * 群发 / 抄送 / 密送
     */
    @Override
    @Async//异步发送（防止接口卡顿）
    public void sendGroup(
            String[] to,
            String[] cc,
            String[] bcc,
            String subject,
            String content,
            boolean html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            if (cc != null && cc.length > 0) helper.setCc(cc);
            if (bcc != null && bcc.length > 0) helper.setBcc(bcc);
            helper.setSubject(subject);
            helper.setText(content, html);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("群发邮件失败", e);
        }
    }
}
