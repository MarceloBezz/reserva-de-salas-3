package br.com.alura.servico_reserva.model.Reserva;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "usuario_id")
//    private Usuario usuario;
    private Long usuarioId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Sala sala; FetchType.LAZY)
    @Column(nullable = false)
    private Long salaId;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
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
