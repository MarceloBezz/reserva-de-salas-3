package br.com.alura.servico_reserva.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import br.com.alura.servico_reserva.model.Reserva.*;
import br.com.alura.servico_reserva.model.sala.DadosSala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.servico_reserva.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.repository.ReservaRepository;
import br.com.alura.servico_reserva.validacoes.IValidacaoReserva;
import jakarta.transaction.Transactional;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository repository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private SalaClient salaClient;

    @Autowired
    private List<IValidacaoReserva> validadores;

    public Reserva agendarReserva(ReservaDTO dados, Usuario usuario) {
        DadosSala salaDesejada = salaClient.buscarSalaPorId(dados.salaId());

        validadores.forEach(v -> v.validar(salaDesejada, dados));

        Reserva reserva = new Reserva(dados, usuario.getId(), salaDesejada.id());
        return repository.save(reserva);
    }

    public DadosReserva buscarReserva(Long idReserva, Usuario usuario) {
        Reserva reserva = repository.findById(idReserva).orElseThrow();
        if (reserva.getUsuarioId() != usuario.getId())
            throw new RegraDeNegocioException("Você não tem acesso a essa reserva!");

        return new DadosReserva(reserva, usuario.getId(), reserva.getSalaId());
    }

    public HashMap<String, List<DadosReserva>> buscarTodasReservas(Usuario usuario) {
        var reservasFuturas = repository
                .findByUsuarioIdAndInicioAfter(usuario.getId(), LocalDateTime.now())
                .stream()
                .map(reserva -> new DadosReserva(reserva, usuario.getId(),
                        reserva.getSalaId()))
                .toList();
        var reservasPassadas = repository
                .findByUsuarioIdAndInicioBefore(usuario.getId(), LocalDateTime.now())
                .stream()
                .map(reserva -> new DadosReserva(reserva, usuario.getId(), reserva.getSalaId()))
                .toList();

        HashMap<String, List<DadosReserva>> reservas = new HashMap<>();
        reservas.put("ProximasReservas", reservasFuturas);
        reservas.put("ReservasPassadas", reservasPassadas);
        return reservas;
    }

    @Transactional
    public void cancelarReserva(Long id, Usuario usuario) {
        Reserva reserva = repository.findById(id).orElseThrow();
        if (!reserva.getUsuarioId().equals(usuario.getId()))
            throw new RegraDeNegocioException("Você não tem acesso a essa reserva!");

        if (reserva.getStatus() == ReservaStatus.CANCELADA)
            throw new RegraDeNegocioException("Esta reserva já está cancelada!");

        reserva.setStatus(ReservaStatus.CANCELADA);
    }

    public List<Long> listarReservasDisponiveis(HorarioReservaDTO dados) {
        List<Long> idSalasAtivas = salaClient.buscarSalasAtivas();
        List<Long> reservasOcupadas = repository.findReservasOcupadas(dados.inicio(), dados.fim());

        return idSalasAtivas.stream()
                .filter(id -> !reservasOcupadas.contains(id))
                .toList();
    }
}
