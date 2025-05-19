package com.example.auditoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa CSRF para facilitar testes 
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/api/register", "/api/login", "/api/qna/ask", "/api/files/**","/api/videos/**","/api/cursos/**","/api/checklist/**","/api/users/**","/api/checklists/**","/api/search/**").permitAll() // Permitir acesso às rotas públicas
                .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
            )
            .formLogin(login -> login.permitAll()) // Permitir login via formulário
            .logout(logout -> logout.permitAll()); // Permitir logout

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
