package br.com.alura.batch.model;

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

//    private ReservaStatus status;
    private int quantidade;

}
