package br.com.alura.servico_log.dto;

import java.time.LocalDateTime;

public record DadosSugestaoAceita(
        Long salaId,
        LocalDateTime inicio,
        LocalDateTime fim,
        int quantidade) {
}