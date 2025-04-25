package cn.enilu.flash.api.mq.use.simple4;

import cn.enilu.flash.api.mq.use.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 4 路由模式
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 配置一个交换机 - 生产者 指定了路由，进行发送消息
 * 然后生产者发送消息，消息者2 -这个队列收到消息，因为消息者2绑定队列，绑定了生产者所绑定路由件
 *
 * @author shengren.yan
 * @create 2022-09-08
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = ConnectionUtil.getConnection();
        // 指定队列
        Channel channel = connection.createChannel();
        // 交换机的名字
        String exchangeName = "test_direct";
        // 创建交换机   ，模式：DIRECT 路由模式
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);

        // 创建队列  （2个队列）
        String queue1Name = "test_direct_queue1";
        String queue2Name = "test_direct_queue2";
        // 声明（创建）队列
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        // 队列与交换机的绑定  （与队列1，绑定的路由件是：xxx1）
        channel.queueBind(queue1Name, exchangeName, "xxx1");
        // 队列与交换机的绑定  （与队列2，绑定的路由件是：yyy1 、yyy2、yyy3）
        channel.queueBind(queue2Name, exchangeName, "yyy1");
        channel.queueBind(queue2Name, exchangeName, "yyy2");
        channel.queueBind(queue2Name, exchangeName, "yyy3");

        // 消息
        String message = "消息信息：路由模式：yyy3";
        // 发送消息（指定路由器、和路由件）
        channel.basicPublish(exchangeName, "yyy3", null, message.getBytes());
        System.out.println(message);
        // 释放资源
        channel.close();
        connection.close();
    }

}
