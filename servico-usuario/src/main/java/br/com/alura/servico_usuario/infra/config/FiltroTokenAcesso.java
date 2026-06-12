package br.com.alura.servico_usuario.infra.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.servico_usuario.model.Usuario.Usuario;
import br.com.alura.servico_usuario.repository.UsuarioRepository;
import br.com.alura.servico_usuario.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final TokenService tokenService;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Recuperar o token da requisição
        String token = recuperarToken(request);

        if (token != null) {
            // Validação do token
            String email = tokenService.verificarToken(token);
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                    usuario.getAuthorities());
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
