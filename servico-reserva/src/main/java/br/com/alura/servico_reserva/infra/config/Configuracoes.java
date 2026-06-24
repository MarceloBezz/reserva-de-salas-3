package br.com.alura.servico_reserva.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class Configuracoes {

    private final FiltroTokenAcesso filtroTokenAcesso;

    public Configuracoes(FiltroTokenAcesso filtroTokenAcesso) {
        this.filtroTokenAcesso = filtroTokenAcesso;
    }

    @Bean
    protected SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/recentes").hasRole("ADMIN")
                        .pathMatchers("/disponiveis").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(filtroTokenAcesso, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    protected PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }
}
