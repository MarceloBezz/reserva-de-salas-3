package br.com.alura.servico_reserva.model.Reserva;

import java.time.LocalDateTime;

public record DadosReservaEmail(
        Long usuarioId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long salaId
) {
    public DadosReservaEmail(Reserva reserva) {
        this(reserva.getUsuarioId(), reserva.getInicio(), reserva.getFim(), reserva.getSalaId());
    }
}
