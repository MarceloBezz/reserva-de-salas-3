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
        return  rabbitTemplate;
    }

    @Bean
    public Queue filaReservasEnviarEmail() {
        return QueueBuilder
                .nonDurable("reservas.enviar-email")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("reservas.ex")
                .build();
    }

    @Bean
    public Binding bindReservasEmail() {
        return  BindingBuilder
                .bind(filaReservasEnviarEmail())
                .to(fanoutExchange());
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

}
