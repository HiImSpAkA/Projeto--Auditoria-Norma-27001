package com.example.auditoria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir requisições de 'localhost:5173' (frontend React)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // O endereço onde o frontend está rodando
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Todos os cabeçalhos são permitidos
                .allowCredentials(true);  // Permite cookies ou credenciais se necessário
    }
}
