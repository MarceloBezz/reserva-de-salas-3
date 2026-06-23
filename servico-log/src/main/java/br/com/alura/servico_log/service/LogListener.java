package br.com.alura.servico_log.service;

import br.com.alura.servico_log.dto.DadosReserva;
import br.com.alura.servico_log.dto.DadosSugestaoAceita;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogListener {

    @KafkaListener(topics = "reserva-topic", groupId = "grupo")
    public void logarReservaFeita(DadosReserva dados) {
        logarReserva("Reserva feita!", dados);
    }

    @KafkaListener(topics = "reserva-delete-log", groupId = "grupo")
    public void logDeletesBatch(DadosReserva dados) {
        logarReserva("Reserva excluída!", dados);
    }

    @KafkaListener(topics = "reserva-lembrete-log", groupId = "grupo")
    public void logLembretesBatch(DadosReserva dados) {
        logarReserva("Email de lembrete enviado!", dados);
    }

    @KafkaListener(topics = "sugestao-aceita", groupId = "grupo")
    public void logSugestaoAceita(DadosReserva dados) {
        System.out.println("""
                
                Sugestão de reserva de sala aceita!
                Dados:
                Sala ID: %s
                Início: %s
                Fim: %s
                Quantidade: %s
                """.formatted(dados.sala(), dados.inicio(), dados.fim(), dados.quantidade()));
    }

    private void logarReserva(String titulo, DadosReserva dados) {
        System.out.println("""
                
                %s
                Dados:
                ID: %s
                ID do usuário: %s
                ID da sala: %s
                Início: %s
                Fim: %s
                Quantidade de pessoas: %d
                
                """.formatted(titulo, dados.id(), dados.usuarioId(), dados.sala(), dados.inicio(), dados.fim(), dados.quantidade()));
    }
}
