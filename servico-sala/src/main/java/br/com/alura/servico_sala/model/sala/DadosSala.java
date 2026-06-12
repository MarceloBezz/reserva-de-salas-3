package br.com.alura.servico_sala.model.sala;

public record DadosSala(
    Long id,
    String nome,
    String descricao,
    int capacidade,
    boolean ativa
) {
    public DadosSala(Sala sala) {
        this(sala.getId(), sala.getNome(), sala.getDescricao(), sala.getCapacidade(),sala.isAtiva());
    }
}
