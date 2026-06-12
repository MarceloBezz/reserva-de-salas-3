package br.com.alura.servico_log.dto;

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
        int quantidade,
        ReservaStatus status
) {}
