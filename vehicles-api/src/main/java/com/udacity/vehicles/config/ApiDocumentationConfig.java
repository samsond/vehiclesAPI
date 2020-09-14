package com.udacity.vehicles.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfig {

    @Bean
    public OpenAPI vehiclesOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Vehicles API")
                        .description("Vehicles API using Java and Spring Boot that can communicate with separate location and pricing services.")
                        .version("v0.0.1")
                        .license(new License().name("MIT License").url("https://github.com/samsond/vehiclesAPI/blob/master/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("README")
                        .url("https://github.com/samsond/vehiclesAPI/blob/master/README.md"));
    }
}
