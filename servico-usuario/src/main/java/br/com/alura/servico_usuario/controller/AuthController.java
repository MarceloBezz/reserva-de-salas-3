package br.com.alura.servico_usuario.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.alura.servico_usuario.model.Usuario.DadosLogin;
import br.com.alura.servico_usuario.model.Usuario.Usuario;
import br.com.alura.servico_usuario.service.TokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DadosLogin dto) throws Exception {
        try {
            var token = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
            var authentication = authenticationManager.authenticate(token);
    
            var usuario = (Usuario) authentication.getPrincipal();
            String tokenUsuario = tokenService.gerarToken(usuario);
            return ResponseEntity.ok().body(tokenUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login incorreto!");
        }
    }
    
}
