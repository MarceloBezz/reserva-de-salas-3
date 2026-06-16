package br.com.alura.servico_reserva.infra.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.service.UsuarioClient;
import br.com.alura.servico_reserva.service.TokenService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class FiltroTokenAcesso implements WebFilter {

    private final TokenService tokenService;
    private final UsuarioClient usuarioClient;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioClient usuarioClient) {
        this.tokenService = tokenService;
        this.usuarioClient = usuarioClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = recuperarToken(exchange);

        if (token == null)
            return chain.filter(exchange);

        String email = tokenService.verificarToken(token);
        return usuarioClient.autenticaPorEmail(email)
                .flatMap(usuario -> {
                    var authorities = usuario.getRoles()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    SecurityContext context = new SecurityContextImpl(auth);

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                });
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
