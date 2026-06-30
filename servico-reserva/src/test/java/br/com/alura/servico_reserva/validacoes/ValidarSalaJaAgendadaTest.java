package br.com.alura.servico_reserva.validacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.Reserva.ReservaStatus;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ValidarSalaJaAgendadaTest {

    @Mock
    private ReservaRepository repository;

    private ValidarSalaJaAgendada validador;

    @BeforeEach
    void setup() {
        validador = new ValidarSalaJaAgendada(repository);
    }

    @Test
    void deveAceitarQuandoNaoExistirReserva() {

        DadosSala sala = new DadosSala(
                1L,
                "Sala A",
                "Sala A",
                20,
                true
        );

        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                5,
                1L
        );

        when(repository.existsBySalaIdAndInicioLessThanAndFimGreaterThanAndStatus(
                anyLong(),
                any(),
                any(),
                any(ReservaStatus.class)))
                .thenReturn(Mono.just(false));

        StepVerifier.create(validador.validar(sala, dto))
                .verifyComplete();
    }

    @Test
    void deveLancarErroQuandoSalaJaEstiverReservada() {

        DadosSala sala = new DadosSala(
                1L,
                "Sala A",
                "Sala A",
                20,
                true
        );

        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                5,
                1L
        );

        when(repository.existsBySalaIdAndInicioLessThanAndFimGreaterThanAndStatus(
                anyLong(),
                any(),
                any(),
                any(ReservaStatus.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(validador.validar(sala, dto))
                .expectErrorSatisfies(erro -> {
                    assertEquals(RegraDeNegocioException.class, erro.getClass());
                    assertEquals(
                            "A sala já está reservada neste horário! Favor consultar horários disponíveis",
                            erro.getMessage());
                })
                .verify();
    }

}