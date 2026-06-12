package br.com.alura.servico_sala.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.servico_sala.model.sala.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllByAtivaTrue();

    @Query("""
          SELECT s.id
          FROM Sala s
          WHERE s.ativa = true
          """)
    List<Long> findAllIds();
}
