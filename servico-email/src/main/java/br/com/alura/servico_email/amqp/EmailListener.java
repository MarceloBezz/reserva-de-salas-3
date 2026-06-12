package br.com.alura.servico_email.amqp;

import br.com.alura.servico_email.model.DadosReservaEmail;
import br.com.alura.servico_email.model.DadosUsuario;
import br.com.alura.servico_email.service.EmailService;
import br.com.alura.servico_email.service.UsuarioClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private EmailService service;

    @RabbitListener(queues = "reservas.enviar-email")
    public void recebeMensagem(@Payload DadosReservaEmail reserva) {
        DadosUsuario usuario = usuarioClient.buscaPorId(reserva.usuarioId());

        service.enviarEmail(usuario, reserva);
    }
}
