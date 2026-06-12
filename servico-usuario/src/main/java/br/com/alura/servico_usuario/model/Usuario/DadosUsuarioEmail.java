package br.com.alura.servico_usuario.model.Usuario;

public record DadosUsuarioEmail(
        String nome,
        String email
) {
    public DadosUsuarioEmail(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail());
    }
}
