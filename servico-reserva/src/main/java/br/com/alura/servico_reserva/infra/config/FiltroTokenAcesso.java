package br.com.alura.servico_reserva.infra.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.servico_reserva.model.usuario.Usuario;
import br.com.alura.servico_reserva.service.UsuarioClient;
import br.com.alura.servico_reserva.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioClient usuarioClient;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioClient usuarioClient) {
        this.tokenService = tokenService;
        this.usuarioClient = usuarioClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Recuperar o token da requisição
        String token = recuperarToken(request);

        if (token != null) {
            // Validação do token
            String email = tokenService.verificarToken(token);
            // UsuarioDTO usuario = usuarioClient.autenticaPorEmail(email);
            Usuario usuario = usuarioClient.autenticaPorEmail(email);
            var authorities = usuario.getRoles().stream().map(SimpleGrantedAuthority::new).toList();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
