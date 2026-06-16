package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import reactor.core.publisher.Mono;

public interface IValidacaoReserva {
    Mono<Void> validar(DadosSala sala, ReservaDTO dto);
}
