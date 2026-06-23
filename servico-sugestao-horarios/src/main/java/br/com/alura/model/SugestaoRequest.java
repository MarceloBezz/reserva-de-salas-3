package br.com.alura.model;

import java.time.LocalDateTime;

public record SugestaoRequest(
        int capacidade,
        LocalDateTime inicio,
        LocalDateTime fim
) {
}
