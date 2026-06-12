package br.com.alura.servico_email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicoEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoEmailApplication.class, args);
	}

}
