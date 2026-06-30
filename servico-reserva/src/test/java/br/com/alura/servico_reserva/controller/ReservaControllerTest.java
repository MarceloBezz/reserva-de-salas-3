package br.com.alura.servico_reserva.controller;

import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import br.com.alura.servico_reserva.service.RedisService;
import br.com.alura.servico_reserva.service.ReservaEventPublisher;
import br.com.alura.servico_reserva.service.SalaClient;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ReservaControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReservaRepository reservaRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @MockBean
    private SalaClient salaClient;

    @MockBean
    private ReservaEventPublisher reservaEventPublisher;

    @MockBean
    private RedisService redisService;

    @BeforeEach
    void setup() {
        reservaRepository.deleteAll().block();
    }

    @Test
    void deveAgendarReservaComSucesso() {
        Usuario usuario = new Usuario(1L, "teste@email.com", List.of("ROLE_USER"));
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fim = inicio.plusHours(2);
        ReservaDTO request = new ReservaDTO(inicio, fim, 5, 10L);

        when(salaClient.buscarSalaPorId(request.salaId()))
                .thenReturn(Mono.just(new DadosSala(10L, "Sala 1", "Sala de teste", 20, true)));
        when(redisService.limparCache(anyString())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + gerarToken(usuario))
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("Reserva de sala concluída!"));

        var reservas = reservaRepository.findAll().collectList().block();

        assertThat(reservas).hasSize(1);
        assertThat(reservas.get(0).getUsuarioId()).isEqualTo(1L);
        assertThat(reservas.get(0).getSalaId()).isEqualTo(10L);
        assertThat(reservas.get(0).getStatus().name()).isEqualTo("ATIVA");
        assertThat(reservas.get(0).getInicio()).isEqualTo(inicio);
        assertThat(reservas.get(0).getFim()).isEqualTo(fim);
        assertThat(reservas.get(0).getQuantidade()).isEqualTo(5);
    }

    @Test
    void naoDeveAgendarQuandoSalaNaoExiste() {
        Usuario usuario = new Usuario(1L, "teste@email.com", List.of("ROLE_USER"));

        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);

        ReservaDTO request = new ReservaDTO(inicio, fim, 5, 99L);

        when(salaClient.buscarSalaPorId(99L))
                .thenReturn(Mono.error(new RuntimeException("Sala não encontrada")));

        webTestClient.post()
                .uri("/agendar")
                .header("Authorization", "Bearer " + gerarToken(usuario))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();

        assertThat(reservaRepository.count().block()).isZero();
    }

    @Test
    void naoDeveAgendarSemToken() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);

        ReservaDTO request = new ReservaDTO(inicio, fim, 5, 10L);

        webTestClient.post()
                .uri("/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void deveBuscarReservaPorId() {

        Long id = criarReserva();

        Usuario usuario = new Usuario(1L, "teste@email.com", List.of("ROLE_USER"));

        webTestClient.get()
                .uri("/" + id)
                .header("Authorization", "Bearer " + gerarToken(usuario))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.sala").isEqualTo(10);
    }

    @Test
    void deveCancelarReserva() {

        Long id = criarReserva();

        Usuario usuario = new Usuario(1L, "teste@email.com", List.of("ROLE_USER"));

        when(redisService.limparCache(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.patch()
                .uri("/cancelar/" + id)
                .header("Authorization", "Bearer " + gerarToken(usuario))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Reserva cancelada com sucesso!");

        var reserva = reservaRepository.findById(id).block();

        assertThat(reserva.getStatus().name())
                .isEqualTo("CANCELADA");
    }

    private String gerarToken(Usuario usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withClaim("roles", usuario.getRoles())
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00")))
                .sign(algoritmo);
    }

    private Long criarReserva() {

        Usuario usuario = new Usuario(1L, "teste@email.com", List.of("ROLE_USER"));

        LocalDateTime inicio = LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        ReservaDTO dto = new ReservaDTO(
                inicio,
                inicio.plusHours(2),
                5,
                10L);

        when(salaClient.buscarSalaPorId(10L))
                .thenReturn(Mono.just(
                        new DadosSala(10L, "Sala", "", 20, true)));

        when(redisService.limparCache(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/agendar")
                .header("Authorization", "Bearer " + gerarToken(usuario))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated();

        return reservaRepository.findAll()
                .blockFirst()
                .getId();
    }
}
