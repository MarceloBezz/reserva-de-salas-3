package br.com.alura.service;

import br.com.alura.infra.kafka.KafkaProducer;
import br.com.alura.model.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparing;

@ApplicationScoped
public class SugestaoService {
    private final SalaClient salaClient;
    private final ReservaClient reservaClient;
    private final KafkaProducer kafkaProducer;

    public SugestaoService(@RestClient SalaClient salaClient,
                           @RestClient ReservaClient reservaClient,
                           KafkaProducer kafkaProducer) {
        this.salaClient = salaClient;
        this.reservaClient = reservaClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<SugestaoResponse> sugerirSalas(SugestaoRequest request) {
        var salasAtivas = salaClient.buscarTodas();
        var salasDisponiveis = reservaClient.buscarDisponiveis(new HorarioDTO(request.inicio(), request.fim()));

//        Retorna as salas disponíveis naquele horário que suportem a capacidade desejada
        return salasAtivas
                .stream()
                .filter(s -> salasDisponiveis.contains(s.id()))
                .filter(s -> s.capacidade() >= request.capacidade())
                .sorted(comparing(SalaDTO::capacidade))
                .map(SugestaoResponse::new)
                .toList();
    }

    public void aceitarSugestao(SugestaoAceita request, String token) {
        Objects.requireNonNull(token, "Informe o token JWT!");

        var salasDisponiveis = reservaClient.buscarDisponiveis(new HorarioDTO(request.inicio(), request.fim()));
        if(!salasDisponiveis.contains(request.sala()))
            throw new RuntimeException("Essa sala não está mais disponível!");

        var response = reservaClient.agendarSala(token, new ReservaDTO(request));
        if (response.getStatus() != 201)
            throw new RuntimeException(response.getEntity());

        kafkaProducer.publicar(request);
    }
}
