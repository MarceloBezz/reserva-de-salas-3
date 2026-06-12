package br.com.alura.servico_reserva.model.sala;

public record DadosSala(
    Long id,
    String nome,
    String descricao,
    int capacidade,
    boolean ativa
) {
}
