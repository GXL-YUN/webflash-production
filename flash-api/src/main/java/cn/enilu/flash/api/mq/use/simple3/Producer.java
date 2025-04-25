package cn.enilu.flash.api.mq.use.simple3;

import cn.enilu.flash.api.mq.use.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 3 发布订阅模式
 * 生产者
 * 演示：分别 先运行 Consumer1 、Consumer2，把消费者进行运行
 * 然后运行 Producer，生产者运行，生产者，产生1个消息，
 * 配置一个交换机 - 交换机绑定 2个队列 / 消费者1 绑定其中一个队列、消费者2 绑定另一个队列
 * 然后生产者发送消息，消息者1、消息者2 都可以收到消息
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
        // 定义交换机的名字
        String exchangeName = "test_fanout";
        // 交换机类型
        //  DIRECT("direct")：定向
        //  FANOUT("fanout")：扇形（广播），发送消息到每一个与之绑定队列。
        //  TOPIC("topic")：通配符的方式
        //  HEADERS("headers")：参数匹配

        // 创建交换机
        // 参数：交换机名称、交换机类型（FANOUT）、是否持久化、自动删除、内部使用、其它参数
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null);

        // 创建队列
        String queue1Name = "test_fanout_queue1";
        String queue2Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        // 绑定队列和交换机
        // 队列和交换机之间的绑定，参数：队列名称、交换机名称、路由键，fanout，routingKey设置为""
        channel.queueBind(queue1Name, exchangeName, "");
        channel.queueBind(queue2Name, exchangeName, "");
        String body = "日志信息，消息 - 调用了";
        // 发送消息
        channel.basicPublish(exchangeName, "", null, body.getBytes());
        // 释放资源
        channel.close();
        connection.close();
    }

}
