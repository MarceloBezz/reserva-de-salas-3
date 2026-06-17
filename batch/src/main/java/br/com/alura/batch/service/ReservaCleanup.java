package br.com.alura.batch.service;

import br.com.alura.batch.model.DadosReserva;
import br.com.alura.batch.model.Reserva;
import br.com.alura.batch.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservaCleanup {
    private final ReservaRepository repository;
    private final KafkaTemplate<String, DadosReserva> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(ReservaCleanup.class);

    public Mono<Void> removerReservasExpiradas() {
        return repository.findByFimBefore(LocalDateTime.now())
                .flatMap(reserva -> repository
                        .delete(reserva)
                        .then(enviarTopicoKafka(reserva))
                        .thenReturn(reserva)
                )
                .count()
                .doOnSuccess(total -> {
                    if (total == 0)
                        log.info("Nenhuma reserva expirada encontrada!");
                    else
                        log.info("{} reservas expiradas removidas", total);
                })
                .doOnError(e ->
                        log.error("Erro ao remover reservas expiradas", e))
                .then();
    }

    private Mono<Void> enviarTopicoKafka(Reserva reserva) {
        return Mono.fromFuture(kafkaTemplate.send(
                        "reserva-delete-log",
                        String.valueOf(reserva.getId()),
                        new DadosReserva(
                                reserva,
                                reserva.getUsuarioId(),
                                reserva.getSalaId()
                        )
                ))
                .doOnSuccess(x -> log.info("Tópico Kafka enviado!"))
                .onErrorMap(e -> new IllegalStateException("Erro ao enviar mensagem Kafka", e))
                .then();

    }
}
