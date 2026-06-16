package br.com.alura.servico_reserva.batch;

import br.com.alura.servico_reserva.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CleanupJob {
    private final ReservaService service;

    @Scheduled(cron = "0 0 2 * * *")
    public void executaLimpeza() {
        service.removerReservasExpiradas().subscribe();
    }
}
