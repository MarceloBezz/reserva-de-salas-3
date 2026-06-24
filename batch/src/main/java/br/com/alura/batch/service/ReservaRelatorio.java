package br.com.alura.batch.service;

import br.com.alura.batch.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservaRelatorio {
    private final ReservaRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ReservaCleanup.class);

    public Mono<Void> gerarRelatorios() {
        return relatorioDiario();
    }

    private Mono<Void> relatorioDiario() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = hoje.plusDays(1).atStartOfDay();

        return repository.findByInicioBetween(inicioDia, fimDia)
                .count()
                .flatMap(totalReservas ->
                        Mono.fromRunnable(() -> {
                            String nomeArquivo = "relatorio-diario-" + hoje + ".txt";
                            String conteudo = """
                                    RELATÓRIO DIÁRIO DE RESERVAS
                                    
                                    Data: %s
                                    Total de reservas realizadas: %d
                                    """.formatted(hoje, totalReservas);

                            try {
                                Path diretorio = Path.of("/relatorios");
                                Files.createDirectories(diretorio);

                                Path arquivo = diretorio.resolve(nomeArquivo);

                                Files.writeString(arquivo, conteudo, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException("Erro ao gerar relatório", e);
                            }
                        }).subscribeOn(Schedulers.boundedElastic())
                )
                .doOnSuccess(x -> log.info("Relatório gerado!"))
                .then();
    }
}
