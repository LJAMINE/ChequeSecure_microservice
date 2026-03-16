package com.chequesecure.banquecentrale.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI banqueCentraleOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChequeSecure - Banque Centrale Service API")
                        .description("API REST pour l'orchestration des certifications et la gestion des agences")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ChequeSecure")
                                .email("contact@chequesecure.ma")));
    }
}
