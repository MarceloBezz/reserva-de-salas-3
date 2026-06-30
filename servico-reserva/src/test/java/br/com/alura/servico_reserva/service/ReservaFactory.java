package br.com.alura.servico_reserva.service;

import java.time.LocalDateTime;

import br.com.alura.servico_reserva.model.Reserva.Reserva;
import br.com.alura.servico_reserva.model.Reserva.ReservaDTO;
import br.com.alura.servico_reserva.model.Reserva.ReservaStatus;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import br.com.alura.servico_reserva.model.usuario.Usuario;

public class ReservaFactory {

    public static Usuario usuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        return usuario;
    }

    public static ReservaDTO reservaDTO() {
        return new ReservaDTO(
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(2),
            10,
            10L
        );
    }

    public static DadosSala dadosSala() {
        return new DadosSala(
                10L,
                "Sala A",
                "Descrição da Sala A",
                20,
                true
        );
    }

    public static Reserva reserva() {
        Reserva reserva = new Reserva();
        reserva.setId(100L);
        reserva.setSalaId(10L);
        reserva.setUsuarioId(1L);
        reserva.setInicio(LocalDateTime.now().plusDays(1));
        reserva.setFim(LocalDateTime.now().plusDays(1).plusHours(2));
        reserva.setStatus(ReservaStatus.ATIVA);

        return reserva;
    }
}
