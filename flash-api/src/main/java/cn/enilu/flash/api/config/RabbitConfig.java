package cn.enilu.flash.api.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host:/}")
    private String virtualHost;


    // 定义交换机
    public static final String EXCHANGE_DIRECT = "exchange.direct";
    public static final String EXCHANGE_FANOUT = "exchange.fanout";
    public static final String EXCHANGE_TOPIC = "exchange.topic";

    // 定义队列
    public static final String QUEUE_ORDER = "queue.order";
    public static final String QUEUE_PAYMENT = "queue.payment";
    public static final String QUEUE_NOTIFY = "queue.notify";

    // 定义路由键
    public static final String ROUTING_KEY_ORDER = "routing.order";
    public static final String ROUTING_KEY_PAYMENT = "routing.payment";
    public static final String ROUTING_KEY_NOTIFY = "routing.notify";
    // 1. 连接工厂
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);

        log.info("初始化-----------------------------------------rabbittMq");
        // 连接池配置
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(25);
        connectionFactory.setChannelCheckoutTimeout(10000);

        // 发布确认
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    // 2. JSON消息转换器
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 3. RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // 开启消息返回机制
        rabbitTemplate.setMandatory(true);

        // 确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功: " + correlationData);
            } else {
                System.out.println("消息发送失败: " + cause);
            }
        });

        // 返回回调
//        rabbitTemplate.setReturnsCallback(returned -> {
//            System.out.println("消息被退回: " + returned.getMessage());
//        });

        // 重试机制
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);

        return rabbitTemplate;
    }

    // 4. RabbitAdmin (关键配置)
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);  // 自动启动
        rabbitAdmin.setIgnoreDeclarationExceptions(true);  // 忽略声明异常

        // 声明默认交换机和队列
        declareDefaultComponents(rabbitAdmin);

        return rabbitAdmin;
    }

    // 5. 声明默认组件
    private void declareDefaultComponents(RabbitAdmin rabbitAdmin) {
        // 声明死信交换机
        DirectExchange dlxExchange = new DirectExchange("exchange.dlx", true, false);
        rabbitAdmin.declareExchange(dlxExchange);

        // 声明死信队列
        Queue dlxQueue = new Queue("queue.dlx.default", true);
        rabbitAdmin.declareQueue(dlxQueue);

        // 绑定死信队列
        Binding dlxBinding = BindingBuilder.bind(dlxQueue)
                .to(dlxExchange)
                .with("key.dlx.default");
        rabbitAdmin.declareBinding(dlxBinding);
    }

    // 6. 业务交换机
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("exchange.order", true, false);
    }

    // 7. 业务队列（带死信配置）
    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "exchange.dlx");
        args.put("x-dead-letter-routing-key", "key.dlx.order");
        args.put("x-message-ttl", 60000);  // 60秒过期
        args.put("x-max-length", 10000);
        return new Queue("queue.order", true, false, false, args);
    }

    // 8. 绑定
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with("key.order.create");
    }

    // 9. 延迟队列
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "exchange.order");
        args.put("x-dead-letter-routing-key", "key.order.process");
        args.put("x-message-ttl", 30000);  // 30秒延迟
        return new Queue("queue.delay.order", true, false, false, args);
    }
}