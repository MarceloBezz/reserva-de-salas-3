package br.com.alura.servico_reserva.validacoes;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.stereotype.Component;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.Reserva.ReservaStatus;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import reactor.core.publisher.Mono;

@Component
public class ValidarSalaJaAgendada implements IValidacaoReserva {

    private final ReservaRepository repository;

    public ValidarSalaJaAgendada(ReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> validar(DadosSala sala, ReservaDTO dto) {
        return repository
                .existsBySalaIdAndInicioLessThanAndFimGreaterThanAndStatus(sala.id(), dto.fim(), dto.inicio(),
                        ReservaStatus.ATIVA)
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new RegraDeNegocioException(
                                "A sala já está reservada neste horário! Favor consultar horários disponíveis"));
                    }
                    return Mono.empty();
                });
    }

}
