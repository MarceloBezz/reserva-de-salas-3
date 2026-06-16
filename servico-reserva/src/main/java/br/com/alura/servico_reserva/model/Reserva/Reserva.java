package br.com.alura.servico_reserva.model.Reserva;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    private Long id;

    @Column("usuario_id")
    private Long usuarioId;

    @Column("sala_id")
    private Long salaId;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    private ReservaStatus status;
    private int quantidade;

    public Reserva(ReservaDTO dados, Long usuarioId, Long salaDesejada) {
        this.usuarioId = usuarioId;
        this.salaId = salaDesejada;
        this.inicio = dados.inicio();
        this.fim = dados.fim();
        this.status = ReservaStatus.ATIVA;
        this.quantidade = dados.quantidade();
    }
}
