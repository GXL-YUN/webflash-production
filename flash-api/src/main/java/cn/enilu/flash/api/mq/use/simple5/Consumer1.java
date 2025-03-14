package cn.good.yan.simple5;

import cn.good.yan.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 5 主题模式（通配符匹配）
 * 生产者
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 队列1 由 消费者1 监听 路由规则（#.error 、 "order.*） 、队列2 由 消费者2 监听 路由规则（*.*"）
 * 演示1：主题模式  - order.info    消费者1、消费者2 都满足，都可以收到消息
 * 演示2：主题模式  - order.info    消费者2 满足，可以收到消息
 * 演示3：主题模式  - order.info    消费者1、消费者2 都满足，都可以收到消息
 * 解除队列的绑定关系，队列1 路由规则（xx.*.xx）队列2 路由规则（xx.#）
 * 演示4：主题模式  - xx.aa.xx      消费者1、消费者2 都满足，都可以收到消息
 * 演示5：主题模式  - xx.cc         消费者2、满足，可以收到消息
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        // 队列名字
        String queue1Name = "test_topic_queue1";
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
