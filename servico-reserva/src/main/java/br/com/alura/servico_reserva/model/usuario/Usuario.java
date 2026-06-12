package br.com.alura.servico_reserva.model.usuario;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Usuario {
    private Long id;
    private String email;
    private List<String> roles = new ArrayList<>();
}
