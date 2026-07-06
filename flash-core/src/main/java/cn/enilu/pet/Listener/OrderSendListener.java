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
public class OrderSendListener {

    @Autowired
    private OrdersServiceImpl ordersServiceImpl;

    @RabbitListener(queues = "order.queue")
    public void close(String orderId, Channel channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            if (!StringUtils.hasText(orderId)) {
                log.warn("收到空的订单ID，跳过处理");
                channel.basicAck(deliveryTag, false);
                return;
            }
            log.info("处理订单发货：{}", orderId);
            ordersServiceImpl.closeOrderIfUnpaid(orderId);
            
            //channel.basicAck(deliveryTag, false);
            log.info("订单处理成功：{}", orderId);
        } catch (Exception e) {
            log.error("处理订单失败，订单ID: {}, 错误: {}", orderId, e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}