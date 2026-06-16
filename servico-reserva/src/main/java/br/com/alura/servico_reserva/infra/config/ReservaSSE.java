package br.com.alura.servico_reserva.infra.config;

import br.com.alura.servico_reserva.model.Reserva.DadosReserva;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class ReservaSSE {

    private final Sinks.Many<DadosReserva> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer();

    public void publicar(DadosReserva evento) {
        var result = sink.tryEmitNext(evento);
        if (result.isFailure())
            System.out.println("Falha ao emitir evento SSE: " + result);
    }

    public Flux<DadosReserva> stream() {
        return sink.asFlux();
    }
}
