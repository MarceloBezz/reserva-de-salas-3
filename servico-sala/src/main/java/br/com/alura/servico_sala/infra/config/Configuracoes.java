package br.com.alura.servico_sala.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Configuracoes {

    private final FiltroTokenAcesso filtroTokenAcesso;

    public Configuracoes(FiltroTokenAcesso filtroTokenAcesso) {
        this.filtroTokenAcesso = filtroTokenAcesso;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.GET, "/**").permitAll();
                    req.requestMatchers(HttpMethod.PATCH, "/desativar/**").hasRole("ADMIN");
                    req.anyRequest().authenticated();
                })
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(filtroTokenAcesso, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable()) // O CSRF deve estar desabilitado para funcionamento do JWT
                .build();
    }

    @Bean
    protected PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
