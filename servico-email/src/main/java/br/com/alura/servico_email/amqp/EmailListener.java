package br.com.alura.servico_email.amqp;

import br.com.alura.servico_email.model.DadosReservaEmail;
import br.com.alura.servico_email.model.DadosUsuario;
import br.com.alura.servico_email.service.EmailService;
import br.com.alura.servico_email.service.UsuarioClient;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailListener {

    private final UsuarioClient usuarioClient;

    private final EmailService service;

    @RabbitListener(queues = "reservas.enviar-email")
    public void recebeMensagem(@Payload DadosReservaEmail reserva) {
        DadosUsuario usuario = usuarioClient.buscaPorId(reserva.usuarioId());

        service.enviarEmail(usuario, reserva);
        System.out.println("Email Enviado!");
    }
}
