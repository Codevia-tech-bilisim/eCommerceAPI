package com.huseyinsen.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "app.exchange";
    public static final String QUEUE_NAME = "app.queue";
    public static final String DLQ_NAME = "app.queue.dlq";
    public static final String ROUTING_KEY = "app.routing.key";

    // Exchange
    @Bean
    public DirectExchange appExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    // Normal Queue (durable, auto-delete false)
    @Bean
    public Queue appQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY + ".dlq")
                .build();
    }

    // Dead Letter Queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_NAME).build();
    }

    // Binding normal queue
    @Bean
    public Binding appBinding(Queue appQueue, DirectExchange appExchange) {
        return BindingBuilder.bind(appQueue).to(appExchange).with(ROUTING_KEY);
    }

    // Binding DLQ
    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, DirectExchange appExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(appExchange).with(ROUTING_KEY + ".dlq");
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Listener Factory with Retry
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAutoStartup(true);
        factory.setMissingQueuesFatal(false); // <- eksik olan ekleme
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return factory;
    }

    // Email Queue
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable("email.queue").build();
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email.exchange", true, false);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with("email.routing.key");
    }

}