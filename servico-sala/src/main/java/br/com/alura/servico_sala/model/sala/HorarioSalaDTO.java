package br.com.alura.servico_sala.model.sala;

import java.time.LocalDateTime;

public record HorarioSalaDTO(
    LocalDateTime inicio,
    LocalDateTime fim
) {
        public HorarioSalaDTO {
        if (fim.isBefore(inicio) || inicio.isAfter(fim) || inicio.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Horários inválidos!");
        
        int horaMinima = 7;
        int horaMaxima = 23;

        if (inicio.getHour() < horaMinima || fim.getHour() > horaMaxima)
            throw new IllegalArgumentException("As reservas só podem ser feitas entre o horário das 07h às 23h!");
    }
}
