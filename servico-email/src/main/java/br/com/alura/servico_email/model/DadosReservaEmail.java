package br.com.alura.servico_email.model;

import java.time.LocalDateTime;

public record DadosReservaEmail(
        Long usuarioId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long salaId
) {
}
