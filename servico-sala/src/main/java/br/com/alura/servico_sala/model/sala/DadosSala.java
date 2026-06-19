package br.com.alura.servico_sala.model.sala;

import java.io.Serializable;

public record DadosSala(
    Long id,
    String nome,
    String descricao,
    int capacidade,
    boolean ativa
) implements Serializable{
    public DadosSala(Sala sala) {
        this(sala.getId(), sala.getNome(), sala.getDescricao(), sala.getCapacidade(),sala.isAtiva());
    }
}
