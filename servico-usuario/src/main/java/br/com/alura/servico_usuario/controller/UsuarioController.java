package br.com.alura.servico_usuario.controller;

import org.springframework.web.bind.annotation.*;

import br.com.alura.servico_usuario.model.Usuario.UsuarioCadastroDTO;
import br.com.alura.servico_usuario.service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid UsuarioCadastroDTO dto) {
        try {
            var usuario = service.cadastrar(dto);
            return ResponseEntity.ok().body(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca-email/{email}")
    public ResponseEntity<Object> buscaPorEmail(@PathVariable String email) {
        try {
          var usuario = service.buscaPorEmail(email);
          return ResponseEntity.ok().body(usuario);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca-id/{id}")
    public ResponseEntity<Object> buscaPorId(@PathVariable Long id) {
        try {
            var usuario = service.buscaPorId(id);
            return ResponseEntity.ok().body(usuario);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
