package br.com.alura.servico_sala.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.servico_sala.infra.exception.RegraDeNegocioException;
import br.com.alura.servico_sala.model.sala.DadosSala;
import br.com.alura.servico_sala.model.sala.HorarioSalaDTO;
import br.com.alura.servico_sala.model.sala.Sala;
import br.com.alura.servico_sala.model.sala.SalaDTO;
import br.com.alura.servico_sala.repository.SalaRepository;
import jakarta.transaction.Transactional;

@Service
public class SalaService {
    @Autowired
    private SalaRepository repository;

    public Sala criarSala(SalaDTO dto) {
        Sala sala = new Sala(dto);
        return repository.save(sala);
    }

    @Transactional
    public void desativarSala(Long id) {
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Forneça um id válido!"));
        sala.setAtiva(false);
    }

    public List<DadosSala> buscarSalas() {
        return repository
                .findAllByAtivaTrue()
                .stream()
                .map(DadosSala::new)
                .toList();
    }

    public DadosSala buscarPorId(Long id) {
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Forneça um id válido!"));
        return new DadosSala(sala);
    }

    public List<Long> buscarIdsSalas() {
        return repository.findAllIds();
    }
}
