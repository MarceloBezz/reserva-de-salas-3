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

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;
    private final RabbitTemplate rabbitTemplate;
    private final KafkaTemplate<String, DadosReserva> kafkaTemplate;

    @PostMapping("/agendar")
    public Mono<ResponseEntity<String>> agendarReserva(@RequestBody @Valid Mono<ReservaDTO> dtoMono,
            @AuthenticationPrincipal Usuario usuario, UriComponentsBuilder uriBuilder) {
        return dtoMono.flatMap(dto -> service.agendarReserva(dto, usuario)
                .doOnNext(reserva -> {
                    rabbitTemplate.convertAndSend("reservas.ex", "", new DadosReservaEmail(reserva));
                    kafkaTemplate.send("reserva-topic", String.valueOf(reserva.getId()),
                            new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()));
                })
                .map(reserva -> {
                    URI uri = uriBuilder.path("/reserva/{idReserva}")
                            .buildAndExpand(reserva.getId())
                            .toUri();
                    return ResponseEntity.created(uri).body("Reserva de sala concluída!");
                }));
    }

    @GetMapping("/{idReserva}")
    public Mono<ResponseEntity<DadosReserva>> verReserva(@PathVariable Long idReserva,
            @AuthenticationPrincipal Usuario usuario) {
        return service.buscarReserva(idReserva, usuario)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/todas")
    public Mono<ResponseEntity<HashMap<String, List<DadosReserva>>>> verTodasReservas(
            @AuthenticationPrincipal Usuario usuario) {
        System.out.println("CHEGOU NO SERVICO");
        return service.buscarTodasReservas(usuario)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/disponiveis")
    public Mono<ResponseEntity<List<Long>>> verReservasDisponiveis(@RequestBody Mono<HorarioReservaDTO> horariosMono) {
        return horariosMono.flatMap(horarios -> service.listarReservasDisponiveis(horarios)
                .map(ResponseEntity::ok));
    }

    @PatchMapping("/cancelar/{id}")
    public Mono<ResponseEntity<String>> cancelarReserva(@PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return service.cancelarReserva(id, usuario)
                .thenReturn(ResponseEntity.ok().body("Reserva cancelada com sucesso!"));
    }

}
