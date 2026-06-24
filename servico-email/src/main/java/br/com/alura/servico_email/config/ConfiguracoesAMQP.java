package br.com.alura.servico_email.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracoesAMQP {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory conn, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(conn);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue filaReservaCriada() {
        return QueueBuilder
                .durable("reservas.enviar-email")
                .build();
    }

    @Bean
    public Queue filaLembreteReserva() {
        return QueueBuilder
                .durable("reservas.lembrete-email")
                .build();
    }

    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder
                .topicExchange("reservas.ex")
                .durable(true)
                .build();
    }

    @Bean
    public Binding bindReservasEmail() {
        return BindingBuilder
                .bind(filaReservaCriada())
                .to(topicExchange())
                .with("reservas.criada");
    }

    @Bean
    public Binding bindLembreteEmail() {
        return BindingBuilder
                .bind(filaLembreteReserva())
                .to(topicExchange())
                .with("reservas.lembrete");
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        RabbitAdmin admin = new RabbitAdmin(conn);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

}
