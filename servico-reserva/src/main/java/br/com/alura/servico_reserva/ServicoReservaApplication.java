package br.com.alura.servico_reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicoReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoReservaApplication.class, args);
	}

}
