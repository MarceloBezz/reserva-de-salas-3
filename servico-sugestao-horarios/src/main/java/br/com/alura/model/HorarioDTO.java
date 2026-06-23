package br.com.alura.model;

import java.time.LocalDateTime;

public record HorarioDTO(
    LocalDateTime inicio,
    LocalDateTime fim
) {}