package com.banquito.gateway.gestion.banquito.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Comercios y POS")
                        .version("1.0")
                        .description("API para la gestión de comercios y sus dispositivos POS en el sistema de pagos Banquito")
                        .contact(new Contact()
                                .name("Banquito")
                                .email("soporte@banquito.com")));
    }
} 