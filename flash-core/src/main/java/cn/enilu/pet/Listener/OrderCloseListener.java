package cn.enilu.pet.Listener;


import cn.enilu.pet.server.OrdersServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 死信队列  保底操作
 */
@Component
public class OrderCloseListener {

    @Autowired
    private OrdersServiceImpl ordersServiceImpl;

    @RabbitListener(queues = "order.dlx.queue")
    public void close(Long orderId) {

        System.out.println("废弃数据：" + orderId);
        ordersServiceImpl.closeOrderIfUnpaid(orderId);
    }
}