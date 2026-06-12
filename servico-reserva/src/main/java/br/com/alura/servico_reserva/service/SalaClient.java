package br.com.alura.servico_reserva.service;

import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("servico-sala")
public interface SalaClient {
    @RequestMapping(method = RequestMethod.GET, value = "/buscar/{id}")
    DadosSala buscarSalaPorId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/todas/ids")
    List<Long> buscarSalasAtivas();
}
