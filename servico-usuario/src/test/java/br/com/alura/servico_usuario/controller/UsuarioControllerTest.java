package br.com.alura.servico_usuario.controller;

import br.com.alura.servico_usuario.model.Usuario.UsuarioCadastroDTO;
import br.com.alura.servico_usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest extends IntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limpaBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCadastrarUsuario() throws Exception {
        UsuarioCadastroDTO request = new UsuarioCadastroDTO(
                "Marcelo",
                "email@email.com",
                "senha123#",
                "11999999999"
        );

        mockMvc.perform(post("/cadastrar")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailEmBranco() throws Exception {
        UsuarioCadastroDTO request = new UsuarioCadastroDTO(
                "Marcelo",
                "",
                "senha123#",
                "11999999999"
        );

        mockMvc.perform(post("/cadastrar")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailEmFormatoIncorreto() throws Exception {
        UsuarioCadastroDTO request = new UsuarioCadastroDTO(
                "Marcelo",
                "emailemailcom",
                "senha123#",
                "11999999999"
        );

        mockMvc.perform(post("/cadastrar")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarUsuarioComNomeEmBranco() throws Exception {
        UsuarioCadastroDTO request = new UsuarioCadastroDTO(
                "",
                "email@email.com",
                "senha123#",
                "11999999999"
        );

        mockMvc.perform(post("/cadastrar")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarUsuarioComSenhaEmBranco() throws Exception {
        UsuarioCadastroDTO request = new UsuarioCadastroDTO(
                "Marcelo",
                "email@email.com",
                "",
                "11999999999"
        );

        mockMvc.perform(post("/cadastrar")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
