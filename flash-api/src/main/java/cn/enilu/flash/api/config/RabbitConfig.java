package cn.enilu.flash.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
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

@Configuration
@Slf4j
public class RabbitConfig {

    /* ================= 基础配置 ================= */

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

    /* ================= MQ 常量 ================= */

    public interface MqConstants {

        String ORDER_EXCHANGE = "order.exchange";
        String ORDER_QUEUE = "order.queue";
        String ORDER_ROUTING_KEY = "order.create";

        /* ===== 延迟关单（核心） ===== */
        String DELAY_EXCHANGE = "order.delay.exchange";
        String DELAY_QUEUE = "order.close.queue";
        String DELAY_ROUTING_KEY = "order.close";

        /* ===== 死信兜底 ===== */
        String ORDER_DLX_EXCHANGE = "order.dlx.exchange";
        String ORDER_DLX_QUEUE = "order.dlx.queue";
        String ORDER_DLX_ROUTING_KEY = "order.dlx";


    }

    /* ================= 连接工厂 ================= */

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);

        factory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        factory.setChannelCacheSize(25);
        factory.setChannelCheckoutTimeout(10_000);

        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        factory.setPublisherReturns(true);

        log.info("RabbitMQ connectionFactory initialized {}","初始化成功");
        return factory;
    }

    /* ================= JSON 序列化 ================= */

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /* ================= RabbitTemplate ================= */

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        template.setMandatory(true);

        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message send success: {}", correlationData);
            } else {
                log.error("Message send failed: {}", cause);
            }
        });
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
        template.setRetryTemplate(retryTemplate);
        return template;
    }

    /* ================= RabbitAdmin ================= */

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.setIgnoreDeclarationExceptions(true);
        return admin;
    }

    /* ================= 死信交换机 & 队列 ================= */

    /***
     * 什么是 DLX（Dead Letter Exchange）
     * DLX = 死信交换机
     * 当一条消息变成 “死信”​ 时：
     * 死信触发条件
     * 说明
     *  消息 TTL 过期
     *  消息被拒绝（reject/nack）
     * 消费失败
     *  队列满了
     * 新消息进不来
     *  RabbitMQ 不会扔掉消息​
     *  而是 转发到指定的 DLX
     * @return
     */

    @Bean
    public DirectExchange orderDlxExchange() {
        //常见交换机
        return new DirectExchange(MqConstants.ORDER_DLX_EXCHANGE, true, false);
    }
    @Bean
    public Queue orderDlxQueue() {
        //创建队列
        return QueueBuilder
                .durable(MqConstants.ORDER_DLX_QUEUE)
                .build();
    }
    @Bean
    public Binding orderDlxBinding() {
        return BindingBuilder
                .bind(orderDlxQueue())
                .to(orderDlxExchange())
                .with(MqConstants.ORDER_DLX_ROUTING_KEY);
    }



    /* ================= 业务交换机 & 队列 ================= */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(MqConstants.ORDER_EXCHANGE, true, false);
    }
    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", MqConstants.ORDER_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", MqConstants.ORDER_DLX_ROUTING_KEY);
        args.put("x-max-length", 10_000);

        return QueueBuilder
                .durable(MqConstants.ORDER_QUEUE)
                .withArguments(args)
                .build();
    }
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(MqConstants.ORDER_ROUTING_KEY);
    }



/*===================================延迟队列=======================================================*/
    /**
     *
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(
                MqConstants.DELAY_EXCHANGE,
                "x-delayed-message",
                true,
                false,
                args
        );
    }
    @Bean
    public Queue delayQueue() {
        return new Queue(MqConstants.DELAY_QUEUE, true);
    }
    @Bean
    public Binding delayBinding() {
        return BindingBuilder
                .bind(delayQueue())
                .to(delayExchange())
                .with(MqConstants.DELAY_ROUTING_KEY)
                .noargs();
    }

    /* ================= 消费者容器工厂 ================= */

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());

        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);

        return factory;
    }
}