package br.com.alura.model;

public record SugestaoResponse(
        Long salaId,
        String nome,
        int capacidade
) {
    public SugestaoResponse(SalaDTO sala) {
        this(sala.id(), sala.nome(), sala.capacidade());
    }
}
