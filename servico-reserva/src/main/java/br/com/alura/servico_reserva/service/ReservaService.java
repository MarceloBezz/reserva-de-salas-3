package br.com.alura.servico_reserva.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import br.com.alura.servico_reserva.model.Reserva.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import br.com.alura.servico_reserva.validacoes.IValidacaoReserva;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservaService {
    private final ReservaRepository repository;
    private final SalaClient salaClient;
    private final List<IValidacaoReserva> validadores;
    private final ReservaEventPublisher publisher;
    private final RedisService redisService;

    private static final String CACHE_DISPONIBILIDADE = "disponibilidade";
    private static final int TTL_CACHE_DISPONIBILIDADE = 5;

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    public Mono<Reserva> agendarReserva(ReservaDTO dados, Usuario usuario) {
        return salaClient.buscarSalaPorId(dados.salaId())
                .flatMap(sala ->
                        Flux.fromIterable(validadores)
                                .flatMap(v -> v.validar(sala, dados))
                                .then(Mono.just(sala))
                )
                .map(salaDesejada -> new Reserva(dados, usuario.getId(), salaDesejada.id()))
                .flatMap(repository::save)
                .flatMap(reserva -> redisService
                        .limparCache(CACHE_DISPONIBILIDADE + ":*")
                        .thenReturn(reserva)
                )
                .doOnNext(reserva ->
                        publisher.publicarReservaCriada(reserva, usuario.getId())
                );
    }

    public Mono<DadosReserva> buscarReserva(Long idReserva, Usuario usuario) {
        return repository.findById(idReserva)
                .switchIfEmpty(Mono.error(new RegraDeNegocioException("Reserva não encontrada!")))
                .flatMap(reserva -> {
                    if (!reserva.getUsuarioId().equals(usuario.getId()))
                        return Mono.error(new RegraDeNegocioException("Você não tem acesso a essa reserva!"));

                    return Mono.just(new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()));
                });
    }

    public Mono<HashMap<String, List<DadosReserva>>> buscarTodasReservas(Usuario usuario) {
        var reservasFuturas = repository
                .findByUsuarioIdAndInicioAfter(usuario.getId(), LocalDateTime.now())
                .map(reserva -> new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()))
                .collectList();

        var reservasPassadas = repository
                .findByUsuarioIdAndInicioBefore(usuario.getId(), LocalDateTime.now())
                .map(reserva -> new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()))
                .collectList();

        return Mono.zip(reservasFuturas, reservasPassadas)
                .map(tuple -> {
                    HashMap<String, List<DadosReserva>> reservas = new HashMap<>();
                    reservas.put("ProximasReservas", tuple.getT1());
                    reservas.put("ReservasPassadas", tuple.getT2());
                    return reservas;
                });
    }

    @Transactional
    public Mono<Void> cancelarReserva(Long id, Usuario usuario) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RegraDeNegocioException("Reserva não encontrada!")))
                .flatMap(reserva -> {
                    if (!reserva.getUsuarioId().equals(usuario.getId())) {
                        return Mono.error(new RegraDeNegocioException("Você não tem acesso a essa reserva!"));
                    }
                    if (reserva.getStatus() == ReservaStatus.CANCELADA) {
                        return Mono.error(new RegraDeNegocioException("Esta reserva já está cancelada!"));
                    }
                    reserva.setStatus(ReservaStatus.CANCELADA);
                    return repository
                            .save(reserva)
                            .then(redisService.limparCache(CACHE_DISPONIBILIDADE + ":*"));
                });
    }

    public Mono<List<Long>> listarReservasDisponiveis(HorarioReservaDTO dados) {
        String chave = CACHE_DISPONIBILIDADE + ":%s:%s".formatted(dados.inicio(), dados.fim());

        return redisService
                .buscarNoCache(chave)
                .switchIfEmpty(
                        Mono.defer(() -> calcularDisponibilidade(dados)
                                .flatMap(resultado -> redisService
                                                .salvarNoCache(chave, resultado, TTL_CACHE_DISPONIBILIDADE)
                                                .thenReturn(resultado)
                                        .doOnNext(x -> log.info("REDIS - CACHE MISS [{}]", chave))
                                )
                        )
                );
    }

    private Mono<List<Long>> calcularDisponibilidade(HorarioReservaDTO dados) {
        var salasAtivas = salaClient.buscarSalasAtivas().collectList();
        var reservasOcupadas = repository.findReservasOcupadas(dados.inicio(), dados.fim()).collectList();

        return Mono.zip(salasAtivas, reservasOcupadas)
                .map(tuple -> tuple.getT1().stream()
                        .filter(id -> !tuple.getT2().contains(id))
                        .toList());
    }
}
