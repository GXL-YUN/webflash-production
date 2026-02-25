package cn.enilu.flash.api.utils.rabbitMq;

import cn.enilu.flash.api.utils.rabbitMq.RabbitMQUtils;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RabbitMQManager {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitMQUtils rabbitMQUtils;

    /**
     * 创建带参数的队列
     */
    public boolean createQueueWithArgs(String queueName, Integer maxLength, Long messageTtl,
                                       String deadLetterExchange, String deadLetterRoutingKey) {
        Map<String, Object> args = new HashMap<>();

        if (maxLength != null) {
            args.put("x-max-length", maxLength);
        }

        if (messageTtl != null) {
            args.put("x-message-ttl", messageTtl);
        }

        if (deadLetterExchange != null) {
            args.put("x-dead-letter-exchange", deadLetterExchange);
        }

        if (deadLetterRoutingKey != null) {
            args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        }

        return rabbitMQUtils.declareQueue(queueName, true, false, false, args);
    }

    /**
     * 创建延迟队列
     */
    public boolean createDelayQueue(String queueName, long delayMillis,
                                    String targetExchange, String targetRoutingKey) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", targetExchange);
        args.put("x-dead-letter-routing-key", targetRoutingKey);
        args.put("x-message-ttl", delayMillis);

        return rabbitMQUtils.declareQueue(queueName, true, false, false, args);
    }

    /**
     * 创建优先级队列
     */
    public boolean createPriorityQueue(String queueName, int maxPriority) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", maxPriority);

        return rabbitMQUtils.declareQueue(queueName, true, false, false, args);
    }

    /**
     * 获取所有队列信息
     */
    public List<Map<String, Object>> getAllQueues() {
        List<Map<String, Object>> queues = new ArrayList<>();

        // 注意：RabbitAdmin 本身不提供获取所有队列的方法
        // 这里需要结合RabbitMQ Management API或使用RabbitTemplate的execute方法

        return queues;
    }

    /**
     * 重新声明所有配置的队列和交换机
     */
    public void redeclareAll() {
        // 重新声明配置文件中定义的Bean
        // 这需要结合具体的业务配置
    }

    /**
     * 健康检查
     */
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();

        // 连接状态
        boolean connected = rabbitMQUtils.isConnected();
        health.put("connected", connected);

        if (connected) {
            // 检查系统队列
            String[] systemQueues = {"queue.order", "queue.dlx.default"};
            Map<String, Boolean> queueHealth = new HashMap<>();

            for (String queue : systemQueues) {
               // boolean exists = rabbitMQUtils.queueExists(queue);
               // queueHealth.put(queue, exists);
            }

            health.put("queues", queueHealth);
            health.put("status", "UP");
        } else {
            health.put("status", "DOWN");
        }

        return health;
    }
}