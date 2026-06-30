package br.com.alura.servico_reserva.validacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ValidarCapacidadeSalaTest {
        private final ValidarCapacidadeSala validador = new ValidarCapacidadeSala();

        @Test
        void deveAceitarQuandoQuantidadeForMenorQueCapacidade() {

                DadosSala sala = new DadosSala(
                                1L,
                                "Sala A",
                                "Descrição da sala",
                                20,
                                true);

                ReservaDTO dto = new ReservaDTO(
                                LocalDateTime.now(),
                                LocalDateTime.now().plusHours(1),
                                10,
                                1L);

                StepVerifier.create(validador.validar(sala, dto))
                                .verifyComplete();
        }

        @Test
        void deveAceitarQuandoQuantidadeForIgualCapacidade() {

                DadosSala sala = new DadosSala(
                                1L,
                                "Sala A",
                                "Descrição da Sala A",
                                20,
                                true);

                ReservaDTO dto = new ReservaDTO(
                                LocalDateTime.now(),
                                LocalDateTime.now().plusHours(1),
                                20,
                                1L);

                StepVerifier.create(validador.validar(sala, dto))
                                .verifyComplete();
        }

        @Test
        void deveLancarErroQuandoQuantidadeExcederCapacidade() {

                DadosSala sala = new DadosSala(
                                1L,
                                "Sala A",
                                "Descrição da Sala A",
                                20,
                                true);

                ReservaDTO dto = new ReservaDTO(
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(1).plusHours(1),
                                21,
                                1L);

                StepVerifier.create(validador.validar(sala, dto))
                                .expectErrorSatisfies(erro -> {
                                        assertEquals(RegraDeNegocioException.class, erro.getClass());
                                        assertEquals("Capacidade da sala excedida!", erro.getMessage());
                                })
                                .verify();
        }

}