package cn.enilu.pet.Listener;

import cn.enilu.pet.server.OrdersServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@Slf4j
public class OrderDlxListener {

    @Autowired
    private OrdersServiceImpl ordersServiceImpl;

    @RabbitListener(queues = "order.dlx.queue")
    public void close(String orderId, Channel channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            if (!StringUtils.hasText(orderId)) {
                log.warn("死信队列收到空的订单ID，跳过处理");
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.warn("处理死信队列消息 - 废弃数据：{}", orderId);
            ordersServiceImpl.closeOrderIfUnpaid(orderId);
            
            channel.basicAck(deliveryTag, false);
            log.info("死信订单处理成功：{}", orderId);
        } catch (Exception e) {
            log.error("死信队列处理失败，订单ID: {}, 错误: {}", orderId, e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}