package cn.good.yan.simple1;

import com.rabbitmq.client.*;

import java.io.IOException;

import static cn.good.yan.util.C.*;


/**
 * 1 简单模式
 * 消费者
 *
 * @author shengren.yan
 * @create 2022-09-08
 */
public class Consumer {

    // 消费者 - 要消费消息
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置参数
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 创建连接 Connection
        Connection connection = factory.newConnection();
        // 创建频道 Channel
        Channel channel = connection.createChannel();
        // 接收消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            // 回调方法,当收到消息后，会自动执行该方法  
            // 参数1. consumerTag：标识  
            // 参数2. envelope：获取一些信息，交换机，路由key...  
            // 参数3. properties：配置信息  
            // 参数4. body：数据  
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag：" + consumerTag);
                System.out.println("Exchange：" + envelope.getExchange());
                System.out.println("RoutingKey：" + envelope.getRoutingKey());
                System.out.println("properties：" + properties);
                System.out.println("body：" + new String(body));
            }
        };

        // 参数1. queue：队列名称  
        // 参数2. autoAck：是否自动确认，类似咱们发短信，发送成功会收到一个确认消息  
        // 参数3. callback：回调对象  
        // 监听程序，用来监听消息
        channel.basicConsume("new_queue", true, consumer);
    }

}