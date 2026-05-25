package cn.enilu.pet.Listener;

import cn.enilu.pet.server.OrdersServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import cn.enilu.pet.server.OrdersServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSendListener {

    @Autowired
    private OrdersServiceImpl ordersServiceImpl;

    @RabbitListener(queues = "order.queue")
    public void close(Long orderId) {

        System.out.println("发货：" + orderId);
        ordersServiceImpl.closeOrderIfUnpaid(orderId);
    }
}