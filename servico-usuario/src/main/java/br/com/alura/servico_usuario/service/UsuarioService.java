package br.com.alura.servico_usuario.service;

import br.com.alura.servico_usuario.model.Usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.alura.servico_usuario.repository.RoleRepository;
import br.com.alura.servico_usuario.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }

    public DadosUsuario cadastrar(UsuarioCadastroDTO dto) throws Exception {
        String senhaCriptografada = encoder.encode(dto.senha());
        var emailJaCadastrado = repository.findByEmailIgnoreCase(dto.email());

        if (emailJaCadastrado.isPresent()) {
            throw new Exception("Usuário já cadastrado!");
        }

        var role = roleRepository.findByNome(RoleNome.CLIENTE);
        Usuario usuario = new Usuario(dto, senhaCriptografada, role);
        repository.save(usuario);
        return new DadosUsuario(usuario);
    }

    public UsuarioDTO buscaPorEmail(String email) throws Exception {
        var usuario = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new Exception("Email inválido!"));
        return new UsuarioDTO(usuario);
    }

    public DadosUsuarioEmail buscaPorId(Long id) throws Exception {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new Exception("ID inválido!"));
        return new DadosUsuarioEmail(usuario);
    }
}
