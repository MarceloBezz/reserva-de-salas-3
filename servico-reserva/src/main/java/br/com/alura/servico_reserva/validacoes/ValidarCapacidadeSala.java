package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.stereotype.Component;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;

@Component
public class ValidarCapacidadeSala implements IValidacaoReserva{

    @Override
    public void validar(DadosSala sala, ReservaDTO dto) {
        if  (sala.capacidade() < dto.quantidade())
            throw new RegraDeNegocioException("Capacidade da sala excedida!");
    }
    
}
