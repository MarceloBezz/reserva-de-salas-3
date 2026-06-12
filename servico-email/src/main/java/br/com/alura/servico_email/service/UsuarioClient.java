package br.com.alura.servico_email.service;

import br.com.alura.servico_email.model.DadosUsuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "servico-usuario")
public interface UsuarioClient {
    @RequestMapping(method = RequestMethod.GET, value = "/busca-id/{id}")
    DadosUsuario buscaPorId(@PathVariable Long id);
}
