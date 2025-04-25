package cn.enilu.flash.api.mq.use.simple2;

import cn.enilu.flash.api.mq.use.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * 2 工作队列模式  - 多个消费者 - 这是机器1
 * 消费者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生10个消息，
 * 分别被 Consumer1 消费5条，Consumer2 消费5条
 *
 * @author shengren.yan
 * @create 2022-09-08
 */
public class Consumer1 {

    static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 接受者 - 接收消息
                System.out.println("Consumer1 机器：" + new String(body));
            }
        };
        // 监听程序，用来监听消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}