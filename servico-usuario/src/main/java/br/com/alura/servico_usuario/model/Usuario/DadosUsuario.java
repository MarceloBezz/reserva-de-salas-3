package br.com.alura.servico_usuario.model.Usuario;

public record DadosUsuario(
    String nome,
    String email,
    String telefone
) {
    public DadosUsuario(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getTelefone());
    }
}
