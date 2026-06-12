package br.com.alura.servico_usuario.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.alura.servico_usuario.model.Usuario.Usuario;

@Service
public class TokenService {

    public String gerarToken(Usuario usuario) throws Exception {
        try {
            var algoritmo = Algorithm.HMAC256("12345678");
            return JWT.create()
                    .withIssuer("Checkpoint Alura")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expiracao(30))
                    .sign(algoritmo);
        } catch (Exception e) {
            throw new Exception("Erro ao gerar token");
        }
    }

    public String verificarToken(String token) {
        DecodedJWT decodedJWT;
        try {
            var algoritmo = Algorithm.HMAC256("12345678");
            JWTVerifier verifier = JWT.require(algoritmo)
                    .withIssuer("Checkpoint Alura")
                    .build();
            
            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar token");
        }
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }
}
