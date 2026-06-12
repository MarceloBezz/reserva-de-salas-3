package br.com.alura.servico_email.service;

import br.com.alura.servico_email.model.DadosReservaEmail;
import br.com.alura.servico_email.model.DadosUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    //TODO Enviar um link para confirmar ou cancelar a reserva
    public void enviarEmail(DadosUsuario usuario, DadosReservaEmail reserva) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(usuario.email());
        message.setFrom("gertuy9@gmail.com");
        message.setSubject("Reserva confirmada!");
        message.setText(formatarMensagem(reserva, usuario.nome()));

        mailSender.send(message);
    }

    private static String formatarMensagem(DadosReservaEmail reserva, String nome) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return """
                Olá, %s!
                Estamos muito felizes pela confirmação da sua reserva!
                Estamos enviando este e-mail apenas para te confirmar que está tudo certo
                Aproveita e já dá uma olhada nos dados da sua reserva:
                
                Início: %s
                Término: %s
                ID da sala: %s
                
                Qualquer coisa é só nos mandar uma mensagem que estaremos à disposição! 
                """.formatted(nome, reserva.inicio().format(formatter), reserva.fim().format(formatter), reserva.salaId());
    }
}
