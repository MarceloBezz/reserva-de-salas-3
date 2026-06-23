package br.com.alura.model;

import java.time.LocalDateTime;

public record ReservaDTO(
        LocalDateTime inicio,
        LocalDateTime fim,
        int quantidade,
        Long salaId
){
    public ReservaDTO(SugestaoAceita sugestaoAceita) {
        this(sugestaoAceita.inicio(), sugestaoAceita.fim(), sugestaoAceita.quantidade(), sugestaoAceita.sala());
    }
}
