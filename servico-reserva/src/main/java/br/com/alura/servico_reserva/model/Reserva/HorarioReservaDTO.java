package br.com.alura.servico_reserva.model.Reserva;

import java.time.LocalDateTime;

public record HorarioReservaDTO(
    LocalDateTime inicio,
    LocalDateTime fim
) {
        public HorarioReservaDTO {
        if (fim.isBefore(inicio) || inicio.isAfter(fim) || inicio.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Horários inválidos!");
        
        int horaMinima = 7;
        int horaMaxima = 23;

        if (inicio.getHour() < horaMinima || fim.getHour() > horaMaxima)
            throw new IllegalArgumentException("As reservas só podem ser feitas entre o horário das 07h às 23h!");
    }
}
