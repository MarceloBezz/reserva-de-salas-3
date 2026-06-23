package br.com.alura.service;

import br.com.alura.model.SalaDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "ms-salas")
public interface SalaClient {

    @GET
    @Path("/todas")
    List<SalaDTO> buscarTodas();
}
