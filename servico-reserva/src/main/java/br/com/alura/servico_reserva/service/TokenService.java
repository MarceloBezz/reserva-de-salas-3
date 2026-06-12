package br.com.alura.servico_reserva.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenService {
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
}
