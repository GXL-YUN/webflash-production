package cn.enilu.flash.api.mq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="hello")
public class ReceiverUtil {
    @RabbitHandler
    public void process(String hello){
        System.out.println("Receiver:"+hello);
    }
}
