package br.com.alura.model;

import java.time.LocalDateTime;

public record SugestaoAceita(
        Long sala,
        LocalDateTime inicio,
        LocalDateTime fim,
        int quantidade
) {
}
