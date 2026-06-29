package br.com.alura.servico_usuario.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.alura.servico_usuario.model.Usuario.Roles;
import br.com.alura.servico_usuario.model.Usuario.Usuario;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    public String gerarToken(Usuario usuario) throws Exception {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId())
                    .withClaim("roles", usuario.getRoles().stream()
                            .map(Roles::getNome)
                            .map(role -> "ROLE_" + role)
                            .toList())
                    .withExpiresAt(expiracao(30))
                    .sign(algoritmo);
        } catch (Exception e) {
            throw new Exception("Erro ao gerar token");
        }
    }

    public String verificarToken(String token) {
        DecodedJWT decodedJWT;
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algoritmo)
                    .withIssuer(issuer)
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
