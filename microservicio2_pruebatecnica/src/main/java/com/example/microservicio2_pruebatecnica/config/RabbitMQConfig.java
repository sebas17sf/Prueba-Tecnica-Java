package com.example.microservicio2_pruebatecnica.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

     public static final String QUEUE_MOVIMIENTOS = "Movimientos";

     public static final String EXCHANGE_DIRECT = "direct_exchange";

     public static final String ROUTING_KEY_MOVIMIENTOS = "routing.Movimientos";

    @Bean
    public Queue movimientosQueue() {
        return QueueBuilder.durable(QUEUE_MOVIMIENTOS).build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    @Bean
    public Binding movimientosBinding(Queue movimientosQueue, DirectExchange exchange) {
        return BindingBuilder.bind(movimientosQueue).to(exchange).with(ROUTING_KEY_MOVIMIENTOS);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
