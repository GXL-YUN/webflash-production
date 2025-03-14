package cn.good.yan.simple4;

import cn.good.yan.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 4 路由模式
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 配置一个交换机 - 生产者 指定了路由，进行发送消息
 * 然后生产者发送消息，消息者2 -这个队列收到消息，因为消息者2绑定队列，绑定了生产者所绑定路由件
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 队列名字
        String queue1Name = "test_direct_queue1";
        channel.queueDeclare(queue1Name, true, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("body：" + new String(body));
                System.out.println("队列 1 消费者1 将消息打印");
            }
        };
        // 监听程序，用来监听消息
        channel.basicConsume(queue1Name, true, consumer);
    }

}
