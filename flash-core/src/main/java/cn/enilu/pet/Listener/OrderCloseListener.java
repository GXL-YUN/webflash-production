package cn.enilu.pet.Listener;

import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.dao.OrdersDao;
import cn.enilu.pet.em.OrderStatus;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class OrderCloseListener {

    @Autowired
    private OrdersDao ordersDao;
    // 简单队列消费
    @RabbitListener(queues = "order.close.queue")
    public void handleDirectMessage(String message, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("收到消息进行高延迟关闭: " + message);

            String orderId = message;
            //订单关闭
            closeOrderIfUnpaid(orderId);

            channel.basicAck(deliveryTag, false);
        } catch (NumberFormatException e) {
            log.error("消息格式错误: " + message);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理订单关闭失败: " + e.getMessage());
            channel.basicNack(deliveryTag, false, false);
        }
    }

    private void closeOrderIfUnpaid(String orderId) {
        Optional<Orders> ordersOpt = ordersDao.findById(orderId);
        
        if (ordersOpt.isPresent()) {
            Orders orders = ordersOpt.get();
            
            // 只有订单状态为待支付时才关闭
            if ("0".equals(orders.getFdStatus())) {
                orders.setFdStatus(OrderStatus.CANCALAYMENT.getCode());
                ordersDao.save(orders);
                log.info("订单已关闭: {}", orderId);
            } else {
                log.info("订单状态不是待支付，无需关闭: {}, 当前状态: {}", orderId, orders.getFdStatus());
            }
        } else {
            log.warn("订单不存在: {}", orderId);
        }
    }
}
