package br.com.alura.batch.service;

import br.com.alura.batch.model.DadosReserva;
import br.com.alura.batch.model.DadosReservaEmail;
import br.com.alura.batch.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservaReminder {
    private final RabbitTemplate rabbitTemplate;
    private final ReservaRepository repository;
    private final KafkaTemplate<String, DadosReserva> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(ReservaCleanup.class);

    public Mono<Void> enviarLembretes() {
        return repository
                .findByInicioBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(1))
                .flatMap(reserva -> {
                    rabbitTemplate.convertAndSend(
                            "reservas.ex",
                            "reservas.lembrete",
                            new DadosReservaEmail(reserva));

                    return Mono.fromFuture(kafkaTemplate.send(
                            "reserva-lembrete-log",
                            String.valueOf(reserva.getId()),
                            new DadosReserva(reserva, reserva.getUsuarioId(), reserva.getSalaId())
                    ));
                })
                .doOnComplete(() -> log.info("Mensagem e tópico enviados!"))
                .then();
    }
}
