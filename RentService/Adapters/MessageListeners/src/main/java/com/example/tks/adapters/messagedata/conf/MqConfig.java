package com.example.tks.adapters.messagedata.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    public static final String EXCHANGE_NAME = "appExchange";
    public static final String USER_CREATE_QUEUE = "user-create-queue";
    public static final String USER_ACTIVE_QUEUE = "user-active-queue";
    public static final String USER_CREATE_COMPENSATION_QUEUE = "user-create-compensation-queue";
    public static final String USER_ACTIVE_COMPENSATION_QUEUE = "user-active-compensation-queue";
    public static final String ROUTING_CREATE_KEY = "messages.create.key";
    public static final String ROUTING_ACTIVE_KEY = "messages.activate.key";
    public static final String ROUTING_CREATE_COMPENSATION_KEY = "messages.create.compensation.key";
    public static final String ROUTING_ACTIVE_COMPENSATION_KEY = "messages.activate.compensation.key";

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userCreateQueue() {
        return new Queue(USER_CREATE_QUEUE);
    }

    @Bean
    public Queue userActiveQueue() {
        return new Queue(USER_ACTIVE_QUEUE);
    }

    @Bean
    public Queue userCreateCompensationQueue() {
        return new Queue(USER_CREATE_COMPENSATION_QUEUE);
    }

    @Bean
    public Queue userActiveCompensationQueue() {
        return new Queue(USER_ACTIVE_COMPENSATION_QUEUE);
    }

    @Bean
    public Binding declareBindingUserCreate() {
        return BindingBuilder.bind(userCreateQueue()).to(appExchange()).with(ROUTING_CREATE_KEY);
    }

    @Bean
    public Binding declareBindingUserActive() {
        return BindingBuilder.bind(userActiveQueue()).to(appExchange()).with(ROUTING_ACTIVE_KEY);
    }

    @Bean
    public Binding declareBindingUserCreateCompensation() {
        return BindingBuilder.bind(userCreateCompensationQueue()).to(appExchange()).with(ROUTING_CREATE_COMPENSATION_KEY);
    }

    @Bean
    public Binding declareBindingUserActiveCompensation() {
        return BindingBuilder.bind(userActiveCompensationQueue()).to(appExchange()).with(ROUTING_ACTIVE_COMPENSATION_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
