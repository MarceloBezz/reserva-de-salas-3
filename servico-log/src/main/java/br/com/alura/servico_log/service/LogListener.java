package br.com.alura.servico_log.service;

import br.com.alura.servico_log.dto.DadosReserva;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogListener {

    @KafkaListener(topics = "reserva-topic", groupId = "grupo")
    public void log(DadosReserva dados) {
        System.out.println("""
                
                Reserva feita!
                Dados:
                ID: %s
                ID do usuário: %s
                ID da sala: %s
                Início: %s
                Fim: %s
                Quantidade de pessoas: %d
                
                """.formatted(dados.id(), dados.usuarioId(), dados.sala(), dados.inicio(), dados.fim(), dados.quantidade()));
    }
}
