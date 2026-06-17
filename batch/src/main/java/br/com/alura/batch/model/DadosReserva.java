package br.com.alura.batch.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DadosReserva(
        Long id,
        Long usuarioId,
        Long sala,
        @JsonFormat(pattern = "yyyy-MM-dd'T'H:mm:ss")
        LocalDateTime inicio,
        @JsonFormat(pattern = "yyyy-MM-dd'T'H:mm:ss")
        LocalDateTime fim,
        int quantidade
) {
    public DadosReserva(Reserva reserva, Long usuarioId, Long salaId) {
        this(reserva.getId(), usuarioId, salaId,reserva.getInicio(),reserva.getFim(),reserva.getQuantidade());
    }
}
