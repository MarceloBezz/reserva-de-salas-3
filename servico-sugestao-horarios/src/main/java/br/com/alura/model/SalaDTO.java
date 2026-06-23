package br.com.alura.model;

public record SalaDTO(
        Long id,
        String nome,
        String descricao,
        int capacidade,
        boolean ativa
) {
}