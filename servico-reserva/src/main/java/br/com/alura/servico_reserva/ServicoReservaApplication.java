package br.com.alura.servico_reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicoReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoReservaApplication.class, args);
	}

}
