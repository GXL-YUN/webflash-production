package cn.enilu.flash.api.mq;

import cn.enilu.flash.api.mq.bean.OrderMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.core.MessageDeliveryMode;

import java.util.Date;
import java.util.UUID;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送订单消息到直连交换机
     */
    public void sendOrderMessage(OrderMessage orderMessage) {
        // 使用CorrelationData来跟踪消息
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        // 设置消息属性
        rabbitTemplate.convertAndSend(
                "exchange.direct",
               "queue.massage",
                orderMessage,
                message -> {
                    // 设置消息持久化
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    // 设置消息过期时间（毫秒）
                    message.getMessageProperties().setExpiration("30000");
                    // 设置消息ID
                    message.getMessageProperties().setMessageId(correlationData.getId());
                    // 设置时间戳
                    message.getMessageProperties().setTimestamp(new Date());
                    return message;
                },
                correlationData
        );

        System.out.println("发送订单消息: " + orderMessage);
    }

    /**
     * 发送支付消息
     */
    public void sendPaymentMessage(OrderMessage paymentMessage) {
        rabbitTemplate.convertAndSend(
                "exchange.direct",
                "queue.massage",
                paymentMessage
        );
        System.out.println("发送支付消息: " + paymentMessage);
    }

    /**
     * 发送延迟消息
     */
    public void sendDelayMessage(String message, int delayTime) {
        MessageProperties properties = new MessageProperties();
        properties.setDelay(delayTime);  // 设置延迟时间
        Message msg = new Message(message.getBytes(), properties);

        rabbitTemplate.convertAndSend(
                "delayed.exchange",
                "delayed.routing",
                msg
        );
    }

    /**
     * 批量发送消息
     */
    public void batchSendMessages() {
        for (int i = 0; i < 100; i++) {
            OrderMessage order = new OrderMessage();
            order.setAction("i");
            rabbitTemplate.convertAndSend(
                    "exchange.direct",
                    "queue.massage",
                    order
            );
        }
    }
}