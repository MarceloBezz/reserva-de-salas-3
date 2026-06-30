package br.com.alura.servico_reserva.validacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import reactor.test.StepVerifier;

class ValidarSalaAtivaTest {

    private final ValidarSalaAtiva validador = new ValidarSalaAtiva();

    @Test
    void deveAceitarSalaAtiva() {

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

        StepVerifier.create(validador.validar(sala, dto))
                .verifyComplete();
    }

    @Test
    void deveLancarErroQuandoSalaEstiverDesativada() {

        DadosSala sala = new DadosSala(
                1L,
                "Sala A",
                "Sala A",
                20,
                false
        );

        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                5,
                1L
        );

        StepVerifier.create(validador.validar(sala, dto))
                .expectErrorSatisfies(erro -> {
                    assertEquals(RegraDeNegocioException.class, erro.getClass());
                    assertEquals(
                            "A sala desejada não está ativa! Favor selecionar outra que esteja",
                            erro.getMessage());
                })
                .verify();
    }

}