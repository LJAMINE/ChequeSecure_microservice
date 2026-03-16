package com.chequesecure.commercant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI commercantOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChequeSecure - Commerçant Service API")
                        .description("API REST pour la saisie, le suivi et la certification des chèques")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ChequeSecure")
                                .email("contact@chequesecure.ma")));
    }
}
