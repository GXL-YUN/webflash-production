package cn.enilu.flash.api.mq.use.simple2;

import cn.enilu.flash.api.mq.use.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;



/**
 * 2 工作队列模式
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生10个消息，
 * 分别被 Consumer1 消费5条，Consumer2 消费5条
 *
 * @author shengren.yan
 * @create 2022-09-08
 */
public class Producer {

    // 队列
    public static final String QUEUE_NAME = "work_queue";

    // 生产者 - 产生消息
    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 指定队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        for (int i = 1; i <= 10; i++) {
            String body = i + "mq - 多个消息----";
            channel.basicPublish("", QUEUE_NAME, null, body.getBytes());
        }
        // 关闭连接
        channel.close();
        connection.close();
    }

}