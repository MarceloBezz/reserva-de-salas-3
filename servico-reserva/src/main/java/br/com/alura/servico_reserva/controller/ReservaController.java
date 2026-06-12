package br.com.alura.servico_reserva.controller;

import br.com.alura.servico_reserva.model.Reserva.DadosReservaEmail;
import br.com.alura.servico_reserva.model.Reserva.HorarioReservaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.servico_reserva.model.Reserva.DadosReserva;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.service.ReservaService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class ReservaController {
    @Autowired
    private ReservaService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private final KafkaTemplate<String, DadosReserva> kafkaTemplate;

    @PostMapping("/agendar")
    public ResponseEntity<String> agendarReserva(@RequestBody @Valid ReservaDTO dto,
            @AuthenticationPrincipal Usuario usuario, UriComponentsBuilder uriBuilder) {
        var reserva = service.agendarReserva(dto, usuario);
        URI uri = uriBuilder.path("/reserva/{idReserva}")
                .buildAndExpand(reserva.getId())
                .toUri();

        // Envio da reserva para a Exchange reservas.ex
        rabbitTemplate.convertAndSend("reservas.ex", "", new DadosReservaEmail(reserva));
        // Envio dos dados de reserva para o serviço de log
        kafkaTemplate.send("reserva-topic", String.valueOf(reserva.getId()), new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()));

        return ResponseEntity.created(uri).body("Reserva de sala concluída!");
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<DadosReserva> verReserva(@PathVariable Long idReserva,
            @AuthenticationPrincipal Usuario usuario) {
        var reserva = service.buscarReserva(idReserva, usuario);
        return ResponseEntity.ok().body(reserva);
    }

    @GetMapping("/todas")
    public ResponseEntity<HashMap<String, List<DadosReserva>>> verTodasReservas(
            @AuthenticationPrincipal Usuario usuario) {
        var reservas = service.buscarTodasReservas(usuario);
        return ResponseEntity.ok().body(reservas);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Long>> verReservasDisponiveis(@RequestBody HorarioReservaDTO horarios) {
        var reservasDisponiveis = service.listarReservasDisponiveis(horarios);
        return  ResponseEntity.ok().body(reservasDisponiveis);
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        service.cancelarReserva(id, usuario);
        return ResponseEntity.ok().body("Reserva cancelada com sucesso!");
    }

}
