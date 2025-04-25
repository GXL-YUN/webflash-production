package cn.enilu.flash.api.mq.use.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static cn.enilu.flash.api.mq.use.util.C.*;

/**
 * 封装工具类 - 连接MQ
 *
 * @author shengren.yan
 * @create 2024-06-07
 */
public class ConnectionUtil {
    public static final String HOST_ADDRESS = IP;

    // 创建连接
    public static Connection getConnection() throws Exception {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost(HOST_ADDRESS);
        // 端口
        factory.setPort(15672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("gxl");
        factory.setUsername("gxl");
        factory.setPassword("gxl");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

    public static void main(String[] args) throws Exception {
        // 查看是否连接成功
        Connection con = ConnectionUtil.getConnection();
        System.out.println(con);
        con.close();
    }

}
