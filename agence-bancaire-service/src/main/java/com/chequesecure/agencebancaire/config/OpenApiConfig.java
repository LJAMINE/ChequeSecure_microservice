package com.chequesecure.agencebancaire.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agenceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChequeSecure - Agence Bancaire Service API")
                        .description("API REST pour la gestion des comptes et la certification des chèques")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ChequeSecure")
                                .email("contact@chequesecure.ma")));
    }
}
