package cn.enilu.flash.api.mq.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 示例队列和交换机
    public static final String EXAMPLE_QUEUE = "example.queue";
    public static final String EXAMPLE_EXCHANGE = "example.exchange";
    public static final String EXAMPLE_ROUTING_KEY = "example.routing.key";

    // 声明队列
    @Bean
    public Queue exampleQueue() {
        return new Queue(EXAMPLE_QUEUE, true);
    }

    // 声明交换机
    @Bean
    public DirectExchange exampleExchange() {
        return new DirectExchange(EXAMPLE_EXCHANGE, true, false);
    }

    // 绑定队列和交换机
    @Bean
    public Binding exampleBinding() {
        return BindingBuilder.bind(exampleQueue()).to(exampleExchange()).with(EXAMPLE_ROUTING_KEY);
    }

    // 配置消息转换器
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 配置RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // 消息发送到交换机确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送到交换机成功: " + correlationData);
            } else {
                System.out.println("消息发送到交换机失败: " + cause);
            }
        });

        // 消息从交换机发送到队列失败回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("消息从交换机发送到队列失败: " + message + ", replyCode: " + replyCode
                    + ", replyText: " + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey);
        });

        return rabbitTemplate;
    }

    // 配置消费者监听器工厂
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认
        return factory;
    }
}