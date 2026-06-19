package br.com.alura.servico_reserva.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.servico_reserva.model.Reserva.Reserva;
import br.com.alura.servico_reserva.model.Reserva.ReservaStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservaRepository extends ReactiveCrudRepository<Reserva, Long> {
    Mono<Boolean> existsBySalaIdAndInicioLessThanAndFimGreaterThanAndStatus(Long salaId, LocalDateTime fimDesejado, LocalDateTime inicioDesejado, ReservaStatus status);

    Flux<Reserva> findByUsuarioIdAndInicioAfter(Long usuarioId, LocalDateTime agora);
    Flux<Reserva> findByUsuarioIdAndInicioBefore(Long usuarioId, LocalDateTime agora);

    @Query("""
            SELECT DISTINCT r.sala_id
            FROM reservas r
            WHERE r.inicio < :fim
            AND r.fim > :inicio
            AND r.status = 'ATIVA'
            """)
    Flux<Long> findReservasOcupadas(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    Flux<Reserva> findByFimBefore(LocalDateTime agora);
}
