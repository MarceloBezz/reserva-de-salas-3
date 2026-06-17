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
    public void enviarEmail(String email, String assunto, String conteudo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("gertuy9@gmail.com");
        message.setSubject(assunto);
        message.setText(conteudo);

        mailSender.send(message);
    }

    //TODO Enviar um link para confirmar ou cancelar a reserva
    public void enviarEmailConfirmacao(DadosUsuario usuario, DadosReservaEmail reserva) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String mensagem = """
                Olá, %s!
                Estamos muito felizes pela confirmação da sua reserva!
                Estamos enviando este e-mail apenas para te confirmar que está tudo certo
                Aproveita e já dá uma olhada nos dados da sua reserva:
                
                Início: %s
                Término: %s
                ID da sala: %s
                
                Qualquer coisa é só nos mandar uma mensagem que estaremos à disposição! 
                """.formatted(usuario.nome(), reserva.inicio().format(formatter), reserva.fim().format(formatter), reserva.salaId());
        enviarEmail(usuario.email(), "Reserva confirmada!", mensagem);
    }

    public void enviarEmailLembrete(DadosUsuario usuario, DadosReservaEmail reserva) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String mensagem = """
                Olá, %s!
                Passando para lembrar que você tem uma reserva de sala em breve!
                Dá uma olhada nos dados da sua reserva para você não esquecer
                
                Início: %s
                Término: %s
                ID da sala: %s
                
                Qualquer coisa é só nos mandar uma mensagem que estaremos à disposição! 
                """.formatted(usuario.nome(), reserva.inicio().format(formatter), reserva.fim().format(formatter), reserva.salaId());
        enviarEmail(usuario.email(), "Lembrete de Reserva!", mensagem);
    }
}
