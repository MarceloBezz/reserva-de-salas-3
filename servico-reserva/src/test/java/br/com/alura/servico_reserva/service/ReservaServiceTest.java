package br.com.alura.servico_reserva.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.Reserva;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import br.com.alura.servico_reserva.validacoes.IValidacaoReserva;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository repository;

    @Mock
    private SalaClient salaClient;

    @Mock
    private ReservaEventPublisher publisher;

    @Mock
    private RedisService redisService;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    private List<IValidacaoReserva> validadores;
    private IValidacaoReserva validador;

    @BeforeEach
    void setup() {
        validadores = new ArrayList<>();
        validador = mock(IValidacaoReserva.class);
        validadores.add(validador);

        reservaService = new ReservaService(
                repository,
                salaClient,
                validadores,
                publisher,
                redisService,
                meterRegistry);
    }

    @Test
    void deveAgendarReserva() {
        Usuario usuario = ReservaFactory.usuario();
        ReservaDTO dto = ReservaFactory.reservaDTO();
        DadosSala sala = ReservaFactory.dadosSala();
        Reserva reserva = ReservaFactory.reserva();

        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(salaClient.buscarSalaPorId(dto.salaId())).thenReturn(Mono.just(sala));
        when(repository.save(any())).thenReturn(Mono.just(reserva));
        when(redisService.limparCache(anyString())).thenReturn(Mono.empty());
        when(validador.validar(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(reservaService.agendarReserva(dto, usuario))
                .expectNext(reserva)
                .verifyComplete();

        verify(repository).save(any());
        verify(redisService).limparCache(anyString());
        verify(publisher).publicarReservaCriada(reserva, usuario.getId());
        verify(counter).increment();
    }

    @Test
    void naoDeveAgendarReservaQueNaoPasseNaValidacao() {
        Usuario usuario = ReservaFactory.usuario();
        ReservaDTO dto = new ReservaDTO(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                21,
                10L);
        DadosSala sala = ReservaFactory.dadosSala();

        when(validador.validar(any(), any()))
                .thenReturn(Mono.error(
                        new RegraDeNegocioException("Capacidade excedida")));
        when(salaClient.buscarSalaPorId(dto.salaId())).thenReturn(Mono.just(sala));

        StepVerifier.create(reservaService.agendarReserva(dto, usuario))
                .expectError(RegraDeNegocioException.class)
                .verify();

        verify(repository, never()).save(any(Reserva.class));
        verify(redisService, never()).limparCache(anyString());
        verify(publisher, never()).publicarReservaCriada(any(Reserva.class), eq(usuario.getId()));
        verify(counter, never()).increment();
    }

    @Test
    void deveLancarErroQuandoReservaNaoExiste() {
        Usuario usuario = ReservaFactory.usuario();
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(reservaService.buscarReserva(10L, usuario))
                .expectError(RegraDeNegocioException.class)
                .verify();
    }

    @Test
    void deveLancarErroQuandoReservaForDeOutroUsuario() {
        Usuario usuario = ReservaFactory.usuario();
        Reserva reserva = ReservaFactory.reserva();
        reserva.setUsuarioId(999L);

        when(repository.findById(anyLong())).thenReturn(Mono.just(reserva));

        StepVerifier.create(reservaService.buscarReserva(10L, usuario))
                .expectError(RegraDeNegocioException.class)
                .verify();
    }
}