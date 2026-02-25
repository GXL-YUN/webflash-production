package cn.enilu.flash.api.controller.mq;

import cn.enilu.flash.api.utils.rabbitMq.RabbitMQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbit/mq")
@Slf4j
public class RabbitMqController {

    @Autowired
    private RabbitMQUtils rabbitMQUtils;

    @GetMapping(value = "/send")
    public String send(){
        String queueName = "delayed.queue";
        String message = "Delayed message";

        // 创建延迟队列
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", queueName);
        args.put("x-message-ttl", 5000); // 5秒延迟

        rabbitMQUtils.declareQueue("delay.queue", true, false, false, args);
        rabbitMQUtils.declareQueue(queueName, true, false, false, null);

        // 发送延迟消息
        for(int i=0;i<10000;i++){
            String messageId = rabbitMQUtils.sendDelayedMessage("", "delay.queue", message, 5000);
            System.out.println("发送: " + messageId);

        }


        // 接收消息
        Object received = rabbitMQUtils.receiveMessage(queueName, 10000);
        System.out.println("消费: " + received);

        return "同步成功";

    }
}
