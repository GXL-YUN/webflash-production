package cn.enilu.flash.api.utils.rabbitMq;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RabbitMQUtilsTest {

    @Autowired
    private RabbitMQUtils rabbitMQUtils;

    @Test
    public void testSendAndReceive() {
        // 测试发送和接收消息
        String queueName = "test.queue";
        String message = "Hello RabbitMQ";

        // 发送消息
        String messageId = rabbitMQUtils.sendToQueue(queueName, message);
        System.out.println("Sent message with ID: " + messageId);

        // 接收消息
        Object received = rabbitMQUtils.receiveMessage(queueName, 5000);
        System.out.println("Received message: " + received);
    }

    @Test
    public void testQueueOperations() {
        String queueName = "temp.queue";
        String exchangeName = "temp.exchange";
        String routingKey = "temp.key";

        // 声明队列
        boolean queueDeclared = rabbitMQUtils.declareQueue(queueName, true, false, false, null);
        System.out.println("Queue declared: " + queueDeclared);

        // 声明交换机
        boolean exchangeDeclared = rabbitMQUtils.declareExchange(exchangeName, "direct", true, false);
        System.out.println("Exchange declared: " + exchangeDeclared);

        // 绑定队列
        boolean bindingCreated = rabbitMQUtils.bindQueueToExchange(queueName, exchangeName, routingKey);
        System.out.println("Binding created: " + bindingCreated);

        // 获取消息数量
        int messageCount = rabbitMQUtils.getMessageCount(queueName);
        System.out.println("Message count: " + messageCount);

        // 删除队列
       // boolean queueDeleted = rabbitMQUtils.deleteQueue(queueName, false, false);
        //System.out.println("Queue deleted: " + queueDeleted);

        // 删除交换机
        boolean exchangeDeleted = rabbitMQUtils.deleteExchange(exchangeName, false);
        System.out.println("Exchange deleted: " + exchangeDeleted);
    }

    @Test
    public void testDelayedMessage() {
        String queueName = "delayed.queue";
        String message = "Delayed message";

        // 创建延迟队列
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", queueName);
        args.put("x-message-ttl", 5000); // 5秒延迟

        rabbitMQUtils.declareQueue("delay.queue", true, false, false, args);
        rabbitMQUtils.declareQueue(queueName, true, false, false, null);

        // 发送延迟消息
        String messageId = rabbitMQUtils.sendDelayedMessage("", "delay.queue", message, 5000);
        System.out.println("Sent delayed message with ID: " + messageId);

        // 接收消息
        Object received = rabbitMQUtils.receiveMessage(queueName, 10000);
        System.out.println("Received delayed message: " + received);
    }
}
