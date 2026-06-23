package br.com.alura.controller;

import br.com.alura.model.SalaDTO;
import br.com.alura.model.SugestaoAceita;
import br.com.alura.model.SugestaoRequest;
import br.com.alura.model.SugestaoResponse;
import br.com.alura.service.SugestaoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Objects;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/sugestoes")
public class SugestaoController {
    private final SugestaoService service;

    @Context
    HttpHeaders headers;

    public SugestaoController(SugestaoService service) {
        this.service = service;
    }

    @POST
    public List<SugestaoResponse> sugerir(SugestaoRequest request) {
        return service.sugerirSalas(request);
    }

    @POST
    @Path("/aceitar")
    public String aceitarSugestao(SugestaoAceita sugestaoAceita) {
        try {
            String token = headers.getHeaderString("Authorization");
            service.aceitarSugestao(sugestaoAceita, token);
            return "Reserva feita!";
        } catch (Exception e) {
            return "Erro ao reservas sala!";
        }
    }
}
