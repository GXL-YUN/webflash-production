package cn.enilu.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送工具类
 */
@Component
public class MailUtils {

    // 邮箱配置
    @Value("${spring.mail.host}")
    private  String SMTP_HOST="smtp.163.com" ;//smtp_host
    private  int SMTP_PORT = 465;
    @Value("${spring.mail.username}")
    private  String FROM_EMAIL="13720661531@163.com" ;
    @Value("${spring.mail.password}")
    private   String AUTH_CODE="PDmW6JgRH3Kcms2U" ;

    /**
     * 创建邮件会话
     */
    private  Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.timeout", "10000");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, AUTH_CODE);
            }
        });
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否是HTML格式
     */
    public  void sendMail(String to, String subject, String content, boolean isHtml)
            throws MessagingException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);

        // 设置发件人
        message.setFrom(new InternetAddress(FROM_EMAIL));
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // 设置主题
        message.setSubject(subject, "UTF-8");
        // 设置发送时间
        message.setSentDate(new Date());

        if (isHtml) {
            // HTML格式
            message.setContent(content, "text/html;charset=UTF-8");
        } else {
            // 文本格式
            message.setText(content, "UTF-8");
        }

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 发送给多个收件人
     */
    public  void sendMailToMultiple(String[] toList, String subject,
                                          String content, boolean isHtml)
            throws MessagingException, AddressException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(FROM_EMAIL));

        // 设置多个收件人
        InternetAddress[] addresses = new InternetAddress[toList.length];
        for (int i = 0; i < toList.length; i++) {
            addresses[i] = new InternetAddress(toList[i]);
        }
        message.setRecipients(Message.RecipientType.TO, addresses);

        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());

        if (isHtml) {
            message.setContent(content, "text/html;charset=UTF-8");
        } else {
            message.setText(content, "UTF-8");
        }

        Transport.send(message);
    }
}