package cn.enilu.pet.Listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ConsumerListener {


    @RabbitListener(queues = "sys.queue", containerFactory = "sysListenerContainerFactory")//指定工厂
    public void close(Message message, Channel channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            
            if (!StringUtils.hasText(body)) {
                log.warn("队列收到空消息，跳过处理");
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("处理队列消息：{}", body);

            channel.basicAck(deliveryTag, false);
            log.info("消息处理成功");
        } catch (Exception e) {
            log.error("队列处理失败，错误: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
