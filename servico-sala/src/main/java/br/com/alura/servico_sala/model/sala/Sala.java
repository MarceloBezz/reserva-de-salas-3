package br.com.alura.servico_sala.model.sala;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "salas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private int capacidade;
    private boolean ativa;

    public Sala(SalaDTO dto) {
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.capacidade = dto.capacidade();
        this.ativa = true;
    }
}
