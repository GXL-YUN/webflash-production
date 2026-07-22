package cn.enilu.mq.linstent;

import cn.enilu.mq.bean.MqMainLog;
import cn.enilu.mq.service.MqMainLogService;
import cn.enilu.util.SpringBeanUtil;
import cn.enilu.util.StringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ConsumerListener {

    @Autowired
    private MqMainLogService mqMainLogService;
    @RabbitListener(queues = "sys.queue", containerFactory = "sysListenerContainerFactory")//指定工厂
    public void close(Message message, Channel channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            //  cn.hutool.json.JSONU til.parseObj(body);
            JSONObject jsonObject = JSONUtil.parseObj(StringUtil.unwrapJsonString(body));
            //chaxun
            MqMainLog mqMainLog= mqMainLogService.getById(jsonObject.getStr("fdId"));
            SpringBeanUtil.getBean(mqMainLog.getFdService()+"service");


            if(mqMainLog!=null){
                try{
                    if (!StringUtils.hasText(body)) {
                        log.warn("队列收到空消息，跳过处理");
                        channel.basicAck(deliveryTag, false);
                        return;
                    }
                    mqMainLog.setFdStatus(3);
                    log.info("处理队列消息成功：{}", body);
                }catch (Exception e){
                    mqMainLog.setFdStatus(2);
                    log.info("处理队列消息失败：{}", body);
                }finally {
                    mqMainLogService.update(mqMainLog);

                }
            }
            channel.basicAck(deliveryTag, false);//手动确认
        } catch (Exception e) {
            log.error("队列处理失败，错误: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
