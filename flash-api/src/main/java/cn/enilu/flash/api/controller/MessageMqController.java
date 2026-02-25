package cn.enilu.flash.api.controller;
import cn.enilu.flash.api.mq.MessageProducer;
import cn.enilu.flash.api.mq.bean.OrderMessage;
import cn.enilu.flash.api.utils.rabbitMq.RabbitMQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/api/rabbit")
public class MessageMqController {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private RabbitMQUtils rabbitMQUtils;

    /**
     * 发送订单消息
     */
    @PostMapping("/order")
    public String sendOrderMessage(@RequestBody OrderMessage orderMessage) {
        messageProducer.sendOrderMessage(orderMessage);

        for(int i=0;i<200;i++){
            String messageId = rabbitMQUtils.sendDelayedMessage("", "delay.queue", "gxl", 5000);
        }

        return "订单消息发送成功";
    }

    /**
     * 发送支付消息
     */
    @PostMapping("/payment")
    public String sendPaymentMessage(@RequestBody OrderMessage paymentMessage) {

        messageProducer.sendPaymentMessage(paymentMessage);
        return "支付消息发送成功";
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/batch")
    public String sendBatchMessages() {
        messageProducer.batchSendMessages();
        return "批量消息发送成功";
    }
}