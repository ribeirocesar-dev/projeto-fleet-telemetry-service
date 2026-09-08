package com.fleet.telemetry.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TELEMETRY_EXCHANGE = "fleet.telemetry.exchange";
    public static final String TELEMETRY_QUEUE = "fleet.telemetry.ingestion.queue";
    public static final String TELEMETRY_ROUTING_KEY = "fleet.telemetry.routingKey";

    @Bean
    public Queue telemetryQueue() {
        return QueueBuilder.durable(TELEMETRY_QUEUE).build();
    }

    @Bean
    public DirectExchange telemetryExchange() {
        return new DirectExchange(TELEMETRY_EXCHANGE);
    }

    @Bean
    public Binding telemetryBinding(Queue telemetryQueue, DirectExchange telemetryExchange) {
        return BindingBuilder
                .bind(telemetryQueue)
                .to(telemetryExchange)
                .with(TELEMETRY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}