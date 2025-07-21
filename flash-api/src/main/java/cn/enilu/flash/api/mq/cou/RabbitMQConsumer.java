package cn.enilu.flash.api.mq.cou;
import cn.enilu.flash.api.mq.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer {

    // 监听指定队列
    @RabbitListener(queues = RabbitMQConfig.EXAMPLE_QUEUE)
    public void processMessage(Message message, Channel channel) throws IOException {
        try {
            String msg = new String(message.getBody());
            System.out.println("接收到消息: " + msg);

            // 业务处理...

            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 处理失败，拒绝消息并重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}