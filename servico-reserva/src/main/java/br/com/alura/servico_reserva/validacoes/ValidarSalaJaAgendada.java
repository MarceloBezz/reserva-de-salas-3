package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.Reserva.ReservaStatus;
import br.com.alura.servico_reserva.repository.ReservaRepository;

@Component
public class ValidarSalaJaAgendada implements IValidacaoReserva{

    @Autowired
    private ReservaRepository repository;

    @Override
    public void validar(DadosSala sala, ReservaDTO dto) {
        if (repository.existsBySalaIdAndInicioLessThanAndFimGreaterThanAndStatus(sala.id(), dto.fim(), dto.inicio(), ReservaStatus.ATIVA))
            throw new RegraDeNegocioException("A sala já está reservada neste horário! Favor consultar horários disponíveis");
    }
     
}
