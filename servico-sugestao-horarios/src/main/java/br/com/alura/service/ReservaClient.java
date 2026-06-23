package br.com.alura.service;

import br.com.alura.model.HorarioDTO;
import br.com.alura.model.ReservaDTO;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RegisterRestClient(configKey = "ms-reservas")
public interface ReservaClient {

    @POST
    @Path("/disponiveis")
    List<Long> buscarDisponiveis(HorarioDTO horario);

    @POST
    @Path("/agendar")
    RestResponse<String> agendarSala(@HeaderParam("Authorization") String token, ReservaDTO dto);
}
