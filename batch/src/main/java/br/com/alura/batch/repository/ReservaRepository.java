package br.com.alura.batch.repository;

import br.com.alura.batch.model.Reserva;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository
        extends ReactiveCrudRepository<Reserva, Long> {

    Flux<Reserva> findByFimBefore(LocalDateTime data);

    Flux<Reserva> findByInicioBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}