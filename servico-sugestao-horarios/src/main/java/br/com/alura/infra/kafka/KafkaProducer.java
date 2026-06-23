package br.com.alura.infra.kafka;

import br.com.alura.model.SugestaoAceita;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class KafkaProducer {

    @Inject
    @Channel("sugestoes")
    Emitter<SugestaoAceita> emitter;

    public void publicar(SugestaoAceita evento) {
        emitter.send(evento);
    }
}
