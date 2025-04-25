package cn.enilu.flash.api.mq.use.simple5;

import cn.enilu.flash.api.mq.use.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 5 主题模式（通配符匹配）
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 队列1 由 消费者1 监听 路由规则（#.error 、 "order.*） 、队列2 由 消费者2 监听 路由规则（*.*"）
 * 演示1：主题模式  - order.info    消费者1、消费者2 都满足，都可以收到消息
 * 演示2：主题模式  - order.info    消费者2 满足，可以收到消息
 * 演示3：主题模式  - order.info    消费者1、消费者2 都满足，都可以收到消息
 * 解除队列的绑定关系，队列1 路由规则（xx.*.xx）队列2 路由规则（xx.#）
 * 演示4：主题模式  - xx.aa.xx      消费者1、消费者2 都满足，都可以收到消息
 * 演示5：主题模式  - xx.cc         消费者2、满足，可以收到消息
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
        String exchangeName = "test_topic";
        // 创建交换机   ，模式：TOPIC 主题模式（通配符匹配）
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);

        // 创建队列  （2个队列）
        String queue1Name = "test_topic_queue1";
        String queue2Name = "test_topic_queue2";
        // 声明（创建）队列
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        // 队列与交换机的绑定  （与队列1，绑定的路由件是：xxx1）
        // 绑定规则：# 匹配一个或多个词，* 匹配不多不少恰好1个词。
        // 演示：队列1 绑定 2个路由规则
//        channel.queueBind(queue1Name, exchangeName, "#.error");
//        channel.queueBind(queue1Name, exchangeName, "order.*");
//        // 演示：队列2 绑定 1个路由规则
//        channel.queueBind(queue2Name, exchangeName, "*.*");

        // 需要解除，上面的队列的路由绑定关系，在管理平台上操作
        channel.queueBind(queue1Name, exchangeName, "xx.*.xx");
        channel.queueBind(queue2Name, exchangeName, "xx.#");

        // 消息
        String message = "消息信息：主题模式：规则“";
        // 发送消息（指定路由器、和路由件）
        // 演示1：主题模式  - order.info    消费者1、消费者2 都满足，都可以收到消息
//        channel.basicPublish(exchangeName,"order.info",null,message.getBytes());
        // 演示2：主题模式  - order.info    消费者2 满足，可以收到消息
//        channel.basicPublish(exchangeName,"goods.info",null,message.getBytes());
//        // 演示3：主题模式  - order.info  消费者1、消费者2 都满足，都可以收到消息
//        channel.basicPublish(exchangeName, "goods.error", null, message.getBytes());

        // 演示4：主题模式 - xx.aa.xx     消费者1、消费者2 都满足，都可以收到消息
//        channel.basicPublish(exchangeName, "xx.aa.xx", null, message.getBytes());
        // 演示5：主题模式 - xx.cc        消费者2、满足，可以收到消息
        channel.basicPublish(exchangeName, "xx.cc", null, message.getBytes());

        System.out.println(message);
        // 释放资源
        channel.close();
        connection.close();
    }

}
