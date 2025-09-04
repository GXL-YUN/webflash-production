package cn.enilu.flash.api.mq.pro;
import cn.enilu.flash.api.mq.config.RabbitMQConfig;
import cn.enilu.flash.api.mq.util.RabbitMQHelper;
import cn.enilu.flash.bean.vo.front.Rets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class RabbitMQProducer {

    @Autowired
    private RabbitMQHelper rabbitMQHelper;

    @GetMapping("/send")
    public Object sendMessage() {
        String message = "Hello RabbitMQ!";
        rabbitMQHelper.send(RabbitMQConfig.EXAMPLE_EXCHANGE,
                RabbitMQConfig.EXAMPLE_ROUTING_KEY,
                message);
        return Rets.success("消息发送成功: " + message);
    }

    @GetMapping("/send/delay")
    public Object sendDelayMessage() {
        String message = "Hello RabbitMQ Delay Message!";
        rabbitMQHelper.sendDelay(RabbitMQConfig.EXAMPLE_EXCHANGE,
                RabbitMQConfig.EXAMPLE_ROUTING_KEY,
                message,
                5000); // 延迟5秒
        return Rets.success("消息发送成功: " + message);
    }
}
