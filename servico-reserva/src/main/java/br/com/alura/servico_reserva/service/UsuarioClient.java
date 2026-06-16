package br.com.alura.servico_reserva.service;

import br.com.alura.servico_reserva.model.usuario.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {

    private final WebClient client;

    public UsuarioClient(@Qualifier("usuarioWebClient") WebClient client) {
        this.client = client;
    }

    public Mono<Usuario> autenticaPorEmail(String email) {
        return client.get()
                .uri("/busca-email/{email}", email)
                .retrieve()
                .bodyToMono(Usuario.class);
    }
}
