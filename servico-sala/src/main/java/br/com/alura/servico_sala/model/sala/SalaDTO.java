package br.com.alura.servico_sala.model.sala;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SalaDTO(
    @NotBlank String nome,
    String descricao,
    @NotNull @Positive int capacidade
) {
    
}
