package com.hs.languagelearningapi.common;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(" Language Learning Module API")
                        .description("The Language Learning Module API is an assessment test " +
                                "for the  Mid-Level Backend Developer position at Zeraki.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("Sielei Herman")
                                .email("hsielei@gmail.com")));
    }
}
