package com.banquito.gateway.gestion.banquito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        log.info("Configurando CORS para permitir peticiones desde https://front-gateway-seven.vercel.app");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        
        config.addAllowedOrigin("https://front-gateway-seven.vercel.app");
        config.setAllowCredentials(true);
        
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");
        
        
        config.addAllowedHeader("*");
        
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/v1/**", config);
        
        return new CorsFilter(source);
    }
} 