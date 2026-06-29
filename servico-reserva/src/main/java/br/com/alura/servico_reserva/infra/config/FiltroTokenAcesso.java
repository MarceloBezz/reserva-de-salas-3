package br.com.alura.servico_reserva.infra.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.alura.servico_reserva.model.usuario.Usuario;
import reactor.core.publisher.Mono;

@Component
public class FiltroTokenAcesso implements WebFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = recuperarToken(exchange);

        if (token == null)
            return chain.filter(exchange);

        var algoritmo = Algorithm.HMAC256(secret);
        DecodedJWT jwt = JWT.require(algoritmo)
                .withIssuer(issuer)
                .build()
                .verify(token);

        String username = jwt.getSubject();
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        Long id = jwt.getClaim("id").asLong();
        Usuario usuarioAutenticado = new Usuario(id, username, roles);
        Collection<GrantedAuthority> authorities = roles.stream()
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
                
        Authentication auth = new UsernamePasswordAuthenticationToken(usuarioAutenticado, null, authorities);
        SecurityContext context = new SecurityContextImpl(auth);

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }

    private String recuperarToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);

        return null;
    }

}
