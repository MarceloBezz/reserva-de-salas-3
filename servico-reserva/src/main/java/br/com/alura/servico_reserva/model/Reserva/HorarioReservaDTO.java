package br.com.alura.servico_reserva.model.Reserva;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;

import java.time.LocalDateTime;

public record HorarioReservaDTO(
    LocalDateTime inicio,
    LocalDateTime fim
) {
        public HorarioReservaDTO {
        if (fim.isBefore(inicio) || inicio.isAfter(fim) || inicio.isBefore(LocalDateTime.now()))
            throw new RegraDeNegocioException("Horários inválidos!");
        
        int horaMinima = 7;
        int horaMaxima = 23;

        if (inicio.getHour() < horaMinima || fim.getHour() > horaMaxima)
            throw new RegraDeNegocioException("As reservas só podem ser feitas entre o horário das 07h às 23h!");
    }
}
