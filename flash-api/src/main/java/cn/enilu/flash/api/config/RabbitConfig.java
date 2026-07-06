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



        /*======消费者处理======*/
        /**
         * 系统业务交换机
         */
        String SYS_EXCHANGE = "sys.exchange";
        /**
         * 系统业务队列
         */
        String SYS_QUEUE = "sys.queue";
        /**
         * 系统创建路由键
         */
        String SYS_CREATE_ROUTING_KEY = "sys.create";


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

        factory.setConnectionTimeout(15000);
        factory.setRequestedHeartBeat(60);

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




    //======================================================================================
    /**
     * 订单业务交换机 (普通消费)
     * durable: true (持久化)
     * autoDelete: false (不自动删除)
     */
    @Bean
    public DirectExchange orderSysExchange() {
        return new DirectExchange(MqConstants.ORDER_EXCHANGE, true, false);
    }

    /**
     * 订单业务队列 (普通消费)
     * 仅做持久化配置，不配置死信转发，用于正常业务处理
     */
    @Bean
    public Queue orderSysQueue() {
        return QueueBuilder
                .durable(MqConstants.ORDER_QUEUE)
                .build();
    }

    /**
     * 订单队列绑定
     * 将订单队列绑定到订单交换机，使用指定的路由键
     */
    @Bean
    public Binding orderSysBinding() {
        return BindingBuilder
                .bind(orderSysQueue())
                .to(orderSysExchange())
                .with(MqConstants.ORDER_ROUTING_KEY);
    }

    /* ================= 系统业务交换机 & 队列 ================= */
    
    /**
     * 系统业务交换机
     * durable: true (持久化)
     * autoDelete: false (不自动删除)
     */
    @Bean
    public DirectExchange sysExchange() {
        return new DirectExchange(MqConstants.SYS_EXCHANGE, true, false);
    }

    /**
     * 系统业务队列
     * durable: true (持久化)
     */
    @Bean
    public Queue sysQueue() {
        return QueueBuilder
                .durable(MqConstants.SYS_QUEUE)
                .build();
    }

    /**
     * 系统业务队列绑定
     * 将系统队列绑定到系统交换机，使用指定的路由键
     */
    @Bean
    public Binding sysBinding() {
        return BindingBuilder
                .bind(sysQueue())
                .to(sysExchange())
                .with(MqConstants.SYS_CREATE_ROUTING_KEY);
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
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 50000);
        args.put("x-overflow", "reject-publish");
        
        return QueueBuilder
                .durable(MqConstants.DELAY_QUEUE)
                .withArguments(args)
                .build();
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

        factory.setErrorHandler(errorHandler());

        return factory;
    }

    @Bean
    public org.springframework.util.ErrorHandler errorHandler() {
        return new org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler(
            cause -> {
                if (cause instanceof org.springframework.amqp.ImmediateAcknowledgeAmqpException) {
                    System.err.println("立即确认异常（消息将被丢弃）: " + cause.getMessage());
                    return true;
                }
                if (cause.getCause() instanceof IllegalArgumentException) {
                    System.err.println("参数错误，拒绝消息: " + cause.getMessage());
                    return true;
                }
                return false;
            }
        );
    }


    @Bean
    public SimpleRabbitListenerContainerFactory sysListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new org.springframework.amqp.support.converter.SimpleMessageConverter());

        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);

        return factory;
    }
}