package br.com.alura.servico_reserva.infra.mensageria.amqp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservaAMQPConfiguration {

    // @Bean
    // public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
    //     return new RabbitAdmin(conn);
    // }

    // @Bean
    // public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
    //     return event -> rabbitAdmin.initialize();
    // }

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

    // @Bean
    // public TopicExchange topicExchange() {
    //     return new TopicExchange("reservas.ex");
    // }
}
