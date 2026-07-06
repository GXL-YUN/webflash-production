package com.alanlee.util.emil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送工具类
 *
 * 外网工具
 */

@Component
@Slf4j
public class MailOutUtils {

    // 邮箱配置
    @Value("${spring.mail.host}")
    private  String SMTP_HOST="mail.naura.com" ;//smtp_host
    private  int SMTP_PORT = 25;
    @Value("${spring.mail.username}")
    private  String FROM_EMAIL="sysadmin@naura.com" ;
    @Value("${spring.mail.password}")
    private   String AUTH_CODE="8d923a5343ADMIN" ;

    /**
     * 创建邮件会话
     */
    private  Session createSession() {
        // 1. 配置邮件服务器参数（对应你的截图设置）
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.naura.com"); // 邮件服务器地址
        props.put("mail.smtp.port", "25");             // 端口 25 (非SSL)
        props.put("mail.smtp.auth", "true");           // 需要认证（对应界面开关）

        props.put("mail.smtp.ssl.enable", "false");    // 明确关闭 SSL

        props.put("mail.smtp.starttls.enable", "false");

        // 超时设置（对应界面的 30000 毫秒）
        props.put("mail.smtp.connectiontimeout", "30000");
        props.put("mail.smtp.timeout", "30000");

        // 2. 创建认证
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "sysadmin@naura.com",  // 认证账号
                        "8d923a5343ADMIN"       // 认证密码
                );
            }
        });
        return session;
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
        log.info("邮件发送是成功发");
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
        log.info("邮件发送是成功发");
    }


    //邮件以图片发送出去
    public  void sendPicMail(String to, String subject, String content, boolean isHtml)
            throws MessagingException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress("https://assets.msn.cn/weathermapdata/1/static/finance/1stparty/FinanceTaskbarIcons/Finance_Stock_Increase_Decrease/Finance_stock_down_green_72x72.png"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());
        if (isHtml) {
            message.setContent(content, "text/html;charset=UTF-8");
        } else {
            message.setText(content, "UTF-8");
        }
        Transport.send(message);
        log.info("图片邮件发送成功");
    }



    public static void main(String[] args) throws MessagingException {
        MailOutUtils mailOutUtils=new MailOutUtils();
        List list =new ArrayList();
        list.add("13720661531@163.com");
        mailOutUtils.sendPicMail("13720661531@163.com","221","w2ewq",true);

    }
}