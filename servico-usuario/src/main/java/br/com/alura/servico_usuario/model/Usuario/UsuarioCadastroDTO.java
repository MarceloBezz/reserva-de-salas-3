package br.com.alura.servico_usuario.model.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.processing.Pattern;

public record UsuarioCadastroDTO(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String senha,
    String telefone
) {
    
}
