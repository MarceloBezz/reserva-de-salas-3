package br.com.alura.servico_usuario.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.alura.servico_usuario.model.Usuario.DadosLogin;
import br.com.alura.servico_usuario.model.Usuario.Usuario;
import br.com.alura.servico_usuario.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DadosLogin dto) throws Exception {
        try {
            var token = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
            var authentication = authenticationManager.authenticate(token);
    
            var usuario = (Usuario) authentication.getPrincipal();
            String tokenUsuario = tokenService.gerarToken(usuario);
            return ResponseEntity.ok().body("Token gerado com sucesso!\n" + tokenUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login incorreto!");
        }
    }
    
}
