package cn.good.yan.simple3;

import cn.good.yan.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 3 发布订阅模式
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 配置一个交换机 - 交换机绑定 2个队列 / 消费者1 绑定其中一个队列、消费者2 绑定另一个队列
 * 然后生产者发送消息，消息者1、消息者2 都可以收到消息
 */
public class Consumer2 {

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 队列名字
        String queue1Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("body：" + new String(body));
                System.out.println("队列 2 消费者2 将消息打印");
            }
        };
        // 监听程序，用来监听消息
        channel.basicConsume(queue1Name, true, consumer);
    }

}
