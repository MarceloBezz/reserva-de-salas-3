package br.com.alura.servico_sala.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.servico_sala.model.sala.Sala;
import br.com.alura.servico_sala.model.sala.SalaDTO;
import br.com.alura.servico_sala.model.usuario.Usuario;
import br.com.alura.servico_sala.repository.SalaRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SalaControllerTest extends IntegrationTest {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SalaRepository repository;

    @BeforeEach
    private void limpaBanco() {
        repository.deleteAll();
    }

    @Test
    void deveCadastrarSala() throws Exception {
        SalaDTO request = new SalaDTO("Nova sala", "Sala super legal", 100);
        Usuario usuarioLogado = new Usuario(1L, "email@email.com", List.of("ROLE_ADMIN"));
        String token = gerarToken(usuarioLogado);

        mockMvc.perform(post("/cadastrar")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    void naoDeveCadastrarSalaSemPermissao() throws Exception {
        SalaDTO request = new SalaDTO("Nova sala", "Sala super legal", 100);
        Usuario usuarioLogado = new Usuario(1L, "email@email.com", List.of("ROLE_USER"));
        String token = gerarToken(usuarioLogado);

        mockMvc.perform(post("/cadastrar")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveDesativarSala() throws Exception {
        SalaDTO request = new SalaDTO("Nova sala", "Sala super legal", 100);
        Sala salaCadastrada = repository.save(new Sala(request));
        Usuario usuarioLogado = new Usuario(1L, "email@email.com", List.of("ROLE_ADMIN"));
        String token = gerarToken(usuarioLogado);

        mockMvc.perform(patch("/desativar/" + salaCadastrada.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveDesativarSalaSemPermissao() throws Exception {
        SalaDTO request = new SalaDTO("Nova sala", "Sala super legal", 100);
        Sala salaCadastrada = repository.save(new Sala(request));
        Usuario usuarioLogado = new Usuario(1L, "email@email.com", List.of("ROLE_USER"));
        String token = gerarToken(usuarioLogado);

        mockMvc.perform(patch("/desativar/" + salaCadastrada.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    String gerarToken(Usuario usuario) {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withClaim("roles", usuario.getRoles())
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00")))
                .sign(algoritmo);
    }
}
