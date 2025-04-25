package cn.enilu.flash.api.mq.use.simple1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 1 简单模式
 * 生产者
 *
 * @author shengren.yan
 * @create 2022-09-08
 */
public class Producer {

    // 生产者 - 产生消息
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 主机地址
        connectionFactory.setHost("124.222.2.102");
        // 端口号：默认为 5672
        connectionFactory.setPort(5672);
        // 主机名称：默认为 /
        connectionFactory.setVirtualHost("gxl");
        // 用户名；默认为guest
        connectionFactory.setUsername("gxl");
        // 密码；默认为guest
        connectionFactory.setPassword("gxl");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建频道
        Channel channel = connection.createChannel();

        // 声明（创建）队列  
        // queue      参数1：队列名称  
        // durable    参数2：是否定义持久化队列，当 MQ 重启之后还在  
        // exclusive  参数3：是否独占本次连接。若独占，只能有一个消费者监听这个队列且 Connection 关闭时删除这个队列
        // autoDelete 参数4：是否在不使用的时候自动删除队列，也就是在没有 消费者的消费 会自动删除
        // arguments  参数5：队列其它参数  
        channel.queueDeclare("new_queue", true, false, false, null);

        // 要发送的信息  
        String message = "你好；xxx生产一个消息123333！";
        // 参数1：交换机名称,如果没有指定则使用默认Default Exchange
        // 参数2：路由key,简单模式可以传递队列名称  
        // 参数3：配置信息  
        // 参数4：消息内容  
        channel.basicPublish("", "new_queue", null, message.getBytes());
        System.out.println("已发送消息：" + message);

        // 关闭资源  
        channel.close();
        connection.close();

    }

}