package br.com.alura.servico_usuario.service;

import br.com.alura.servico_usuario.model.Usuario.Usuario;
import br.com.alura.servico_usuario.model.Usuario.UsuarioCadastroDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alura.servico_usuario.repository.RoleRepository;
import br.com.alura.servico_usuario.repository.UsuarioRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @Captor
    ArgumentCaptor<Usuario> captor;

    @Test
    void deveCadastrarUsuario() throws Exception {
        var dto = criaDtoCadastro();

        usuarioService.cadastrar(dto);
        verify(usuarioRepository).save(captor.capture());
        Usuario usuarioSalvo = captor.getValue();

        assertEquals(dto.nome(), usuarioSalvo.getNome());
        assertEquals(dto.email(), usuarioSalvo.getEmail());
        assertEquals(dto.telefone(), usuarioSalvo.getTelefone());
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailJaCadastrado() throws Exception {
        var dto = criaDtoCadastro();

        Mockito.when(usuarioRepository.findByEmailIgnoreCase(dto.email())).thenReturn(Optional.of(new Usuario()));

        assertThrows(Exception.class, () -> usuarioService.cadastrar(dto), "Usuário já cadastrado!");
    }

    UsuarioCadastroDTO criaDtoCadastro() {
        return new UsuarioCadastroDTO("Novo usuário", "email@email.com", "senha123$", "11999999999");
    }
}
