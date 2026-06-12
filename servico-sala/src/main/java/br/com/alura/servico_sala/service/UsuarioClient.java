package br.com.alura.servico_sala.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.alura.servico_sala.model.usuario.Usuario;

@FeignClient(name = "servico-usuario")
public interface UsuarioClient {
    @RequestMapping(method = RequestMethod.GET, value = "/busca-email/{email}")
    Usuario autenticaPorEmail(@PathVariable String email);
}
