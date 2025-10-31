package com.hitss.springboot.task_manager.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwagggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                    .info(
                        new Info()
                            .title("Product API")
                            .version("1.0.0")
                            .description("API documentation for Product built with Spring Boot 3.5.7"))
                    .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                    .components(new Components()
                        .addSecuritySchemes(securitySchemeName, 
                            new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)));
    }
}