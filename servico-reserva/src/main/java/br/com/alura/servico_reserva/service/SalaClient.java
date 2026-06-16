package br.com.alura.servico_reserva.service;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SalaClient {

    private final WebClient client;

    public SalaClient(@Qualifier("salaWebClient") WebClient client) {
        this.client = client;
    }

    public Mono<DadosSala> buscarSalaPorId(Long id) {
        return client.get()
                .uri("/buscar/{id}", id)
                .retrieve()
                .bodyToMono(DadosSala.class);
    }

    public Flux<Long> buscarSalasAtivas() {
        return client.get()
                .uri("/todas/ids")
                .retrieve()
                .bodyToFlux(Long.class);
    }
}
