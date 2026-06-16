package br.com.alura.servico_sala.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.alura.servico_sala.model.sala.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllByAtivaTrue();

    @Query("""
          SELECT s.id
          FROM Sala s
          WHERE s.ativa = true
          """)
    List<Long> findAllIds();
}
