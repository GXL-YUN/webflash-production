package cn.enilu.flash.api.utils.rabbitMq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RabbitMQUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private MessageConverter messageConverter;

    /**
     * 发送消息到指定交换机
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @return 消息ID
     */
    public String sendToExchange(String exchange, String routingKey, Object message) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        return messageId;
    }

    /**
     * 发送消息到指定队列
     * @param queueName 队列名称
     * @param message 消息内容
     * @return 消息ID
     */
    public String sendToQueue(String queueName, Object message) {
        return sendToExchange("", queueName, message);
    }

    /**
     * 发送延迟消息
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @param delayMillis 延迟时间(毫秒)
     * @return 消息ID
     */
    public String sendDelayedMessage(String exchange, String routingKey, Object message, long delayMillis) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        rabbitTemplate.convertAndSend(exchange, routingKey, message, m -> {
            m.getMessageProperties().setExpiration(String.valueOf(delayMillis));
            return m;
        }, correlationData);

        return messageId;
    }

    /**
     * 接收消息
     * @param queueName 队列名称
     * @param timeout 超时时间(毫秒)
     * @return 消息内容
     */
    public Object receiveMessage(String queueName, long timeout) {
        return rabbitTemplate.receiveAndConvert(queueName, timeout);
    }

    /**
     * 声明队列
     * @param queueName 队列名称
     * @param durable 是否持久化
     * @param exclusive 是否排他
     * @param autoDelete 是否自动删除
     * @param arguments 队列参数
     * @return 是否声明成功
     */
    public boolean declareQueue(String queueName, boolean durable, boolean exclusive,
                                boolean autoDelete, Map<String, Object> arguments) {
        try {
            Queue queue = new Queue(queueName, durable, exclusive, autoDelete, arguments);
            rabbitAdmin.declareQueue(queue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 声明交换机
     * @param exchangeName 交换机名称
     * @param type 交换机类型(direct/topic/fanout/headers)
     * @param durable 是否持久化
     * @param autoDelete 是否自动删除
     * @return 是否声明成功
     */
    public boolean declareExchange(String exchangeName, String type,
                                   boolean durable, boolean autoDelete) {
        try {
            AbstractExchange exchange = createExchange(exchangeName, type, durable, autoDelete);
            rabbitAdmin.declareExchange(exchange);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private AbstractExchange createExchange(String exchangeName, String type,
                                            boolean durable, boolean autoDelete) {
        switch (type.toLowerCase()) {
            case "direct":
                return new DirectExchange(exchangeName, durable, autoDelete);
            case "topic":
                return new TopicExchange(exchangeName, durable, autoDelete);
            case "fanout":
                return new FanoutExchange(exchangeName, durable, autoDelete);
            case "headers":
                return new HeadersExchange(exchangeName, durable, autoDelete);
            default:
                throw new IllegalArgumentException("不支持的交换机类型: " + type);
        }
    }

    /**
     * 绑定队列到交换机
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param routingKey 路由键
     * @return 是否绑定成功
     */
    public boolean bindQueueToExchange(String queueName, String exchangeName, String routingKey) {
        try {
            Binding binding = new Binding(queueName,
                    Binding.DestinationType.QUEUE,
                    exchangeName,
                    routingKey,
                    null);
            rabbitAdmin.declareBinding(binding);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取队列消息数量
     * @param queueName 队列名称
     * @return 消息数量
     */
    public int getMessageCount(String queueName) {
        Properties properties = rabbitAdmin.getQueueProperties(queueName);
        if (properties != null && properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT) != null) {
            return Integer.parseInt(properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT).toString());
        }
        return 0;
    }

    /**
     * 删除队列
     * @param queueName 队列名称
     * @param ifUnused 是否只在未使用时删除
     * @param ifEmpty 是否只在空时删除
     * @return 是否删除成功
     */
    public void deleteQueue(String queueName, boolean ifUnused, boolean ifEmpty) {
         rabbitAdmin.deleteQueue(queueName, ifUnused, ifEmpty);
    }

    /**
     * 删除交换机
     * @param exchangeName 交换机名称
     * @param ifUnused 是否只在未使用时删除
     * @return 是否删除成功
     */
    public boolean deleteExchange(String exchangeName, boolean ifUnused) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }

    /**
     * 检查连接是否正常
     * @return 是否连接正常
     */
    public boolean isConnected() {
        try {
            return rabbitTemplate.execute(channel -> channel.isOpen());
        } catch (Exception e) {
            return false;
        }
    }
}
