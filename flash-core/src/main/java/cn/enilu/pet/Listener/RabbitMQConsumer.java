package cn.enilu.pet.Listener;


import cn.enilu.flash.bean.entity.message.Message;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 延迟消息
 */
@Component
public class RabbitMQConsumer {

    // 简单队列消费
    @RabbitListener(queues = "order.close.queue")
    public void handleDirectMessage(String message, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            System.out.println("收到消息进行高延迟关闭: " + message);
            // 业务处理逻辑...

            // 手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 处理失败，拒绝消息
            // basicNack: 拒绝并重新入队
            // basicReject: 拒绝，可指定是否重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    // 带重试机制的消费
//    @RabbitListener(queues = "retry.queue")
//    @RabbitRetryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
//    public void handleWithRetry(Message message) {
//        // 处理消息，失败会自动重试
//    }
}