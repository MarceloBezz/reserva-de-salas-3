package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;

public interface IValidacaoReserva {
    void validar(DadosSala sala, ReservaDTO dto);
}
