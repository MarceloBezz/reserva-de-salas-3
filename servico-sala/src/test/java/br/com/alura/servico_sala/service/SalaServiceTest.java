package br.com.alura.servico_sala.service;

import br.com.alura.servico_sala.model.sala.DadosSala;
import br.com.alura.servico_sala.model.sala.Sala;
import br.com.alura.servico_sala.model.sala.SalaDTO;
import br.com.alura.servico_sala.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalaServiceTest {
    @InjectMocks
    private SalaService service;

    @Mock
    private SalaRepository repository;

    @Test
    void deveCriarSala() {
        SalaDTO dto = criaDto();
        when(repository.save(any(Sala.class))).thenReturn(new Sala(dto));

        DadosSala salaCriada = service.criarSala(dto);

        assertEquals(dto.nome(), salaCriada.nome());
        assertEquals(dto.descricao(), salaCriada.descricao());
        assertEquals(dto.capacidade(), salaCriada.capacidade());
    }

    @Test
    void deveDesativarSala() {
        Sala sala = new Sala(criaDto());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(sala));

        service.desativarSala(1L);

        assertFalse(sala.isAtiva());
    }

    SalaDTO criaDto() {
        return new SalaDTO("sala nova", "bela sala", 100);
    }
}
