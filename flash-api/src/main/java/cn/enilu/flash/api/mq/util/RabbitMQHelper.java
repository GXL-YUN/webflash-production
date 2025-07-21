package cn.enilu.flash.api.mq.util;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RabbitMQHelper {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定交换机
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     */
    public void send(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送延迟消息
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @param delay 延迟时间(毫秒)
     */
    public void sendDelay(String exchange, String routingKey, Object message, int delay) {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(delay);
        Message msg = messageConverter.toMessage(message, messageProperties);
        rabbitTemplate.send(exchange, routingKey, msg);
    }

    /**
     * 接收消息
     * @param queueName 队列名称
     * @return 消息内容
     */
    public Object receive(String queueName) {
        return rabbitTemplate.receiveAndConvert(queueName);
    }

    /**
     * 接收消息并指定超时时间
     * @param queueName 队列名称
     * @param timeout 超时时间(毫秒)
     * @return 消息内容
     */
    public Object receive(String queueName, long timeout) {
        return rabbitTemplate.receiveAndConvert(queueName, timeout);
    }

    /**
     * 发送消息并等待响应
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @return 响应结果
     */
    public Object sendAndReceive(String exchange, String routingKey, Object message) {
        return rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
    }

    /**
     * 发送消息并等待响应(带超时)
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @param timeout 超时时间(毫秒)
     * @return 响应结果
     */
    public Object sendAndReceive(String exchange, String routingKey, Object message, long timeout) {
        return rabbitTemplate.convertSendAndReceive(exchange, routingKey, message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setExpiration(String.valueOf(timeout));
                    return messagePostProcessor;
                });
    }
}