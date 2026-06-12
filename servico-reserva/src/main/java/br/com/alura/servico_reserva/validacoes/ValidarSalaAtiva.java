package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.stereotype.Component;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;

@Component
public class ValidarSalaAtiva implements IValidacaoReserva{

    @Override
    public void validar(DadosSala sala, ReservaDTO dto) {
        if (!sala.ativa())
            throw new RegraDeNegocioException("A sala desejada não está ativa! Favor selecionar outra que esteja");
    }
    
}
