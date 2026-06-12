package br.com.alura.servico_reserva.model.Reserva;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Positive;

public record ReservaDTO(
    LocalDateTime inicio,
    LocalDateTime fim,
    @Positive int quantidade,
    Long salaId
) {
    public ReservaDTO {
        if (fim.isBefore(inicio) || inicio.isAfter(fim) || inicio.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Horários inválidos!");
        
        int horaMinima = 7;
        int horaMaxima = 23;

        if (inicio.getHour() < horaMinima || fim.getHour() > horaMaxima)
            throw new IllegalArgumentException("As reservas só podem ser feitas entre o horário das 07h às 23h!");
    }
}
