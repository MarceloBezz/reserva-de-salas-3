package br.com.alura.servico_reserva.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient usuarioWebClient(@Value("${servico.usuario.base-url}") String baseUrl) {
        return webClientBuilder().baseUrl(baseUrl).build();
    }

    @Bean
    public WebClient salaWebClient(@Value("${servico.sala.base-url}") String baseUrl) {
        return webClientBuilder().baseUrl(baseUrl).build();
    }
}
