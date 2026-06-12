package br.com.alura.servico_usuario.model.Usuario;

import java.util.List;

public record UsuarioDTO(
    String email,
    Long id,
    List<String> roles
) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getEmail(), usuario.getId(), usuario.getRoles().stream().map(role -> role.getNome().name()).toList());
    }
}
