package br.com.alura.servico_reserva.service;

import br.com.alura.servico_reserva.infra.config.ReservaSSE;
import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.DadosReserva;
import br.com.alura.servico_reserva.model.Reserva.DadosReservaEmail;
import br.com.alura.servico_reserva.model.Reserva.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservaEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final KafkaTemplate<String, DadosReserva> kafkaTemplate;
    private final ReservaSSE eventoPublisher;

    public void publicarReservaCriada(Reserva reserva, Long usuarioId) {
        rabbitTemplate.convertAndSend(
                "reservas.ex",
                "",
                new DadosReservaEmail(reserva)
        );

        kafkaTemplate.send(
                "reserva-topic",
                String.valueOf(reserva.getId()),
                new DadosReserva(reserva, usuarioId, reserva.getSalaId())
        );

        eventoPublisher.publicar(
                new DadosReserva(reserva, usuarioId, reserva.getSalaId())
        );
    }

    public Mono<Void> publicarReservaExpiradaDeletada(Reserva reserva) {
        return Mono.fromFuture(kafkaTemplate.send(
                        "reserva-delete-log",
                        String.valueOf(reserva.getId()),
                        new DadosReserva(
                                reserva,
                                reserva.getUsuarioId(),
                                reserva.getSalaId()
                        )
                ))
                .onErrorMap(e -> new RegraDeNegocioException("Erro ao enviar mensagem Kafka: " + e.getMessage()))
                .then();
    }
}
