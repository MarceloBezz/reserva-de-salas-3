package br.com.alura.servico_usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.servico_usuario.model.Usuario.RoleNome;
import br.com.alura.servico_usuario.model.Usuario.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long>{
    Roles findByNome(RoleNome nome);
}
