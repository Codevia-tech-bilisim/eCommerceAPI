package com.huseyinsen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("e-Commerce Application API")
                        .version("1.0.0")
                        .description("Comprehensive API documentation with authentication and error responses"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        // JWT Security Scheme
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                        // Örnek Error Response
                        .addResponses("Unauthorized",
                                new ApiResponse()
                                        .description("JWT token missing or invalid")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        // Örnek DTO schema
                        .addSchemas("ErrorResponse",
                                new Schema<>().type("object").properties(Map.of(
                                        "timestamp", new Schema<>().type("string").example("2025-08-13T12:34:56"),
                                        "status", new Schema<>().type("integer").example(401),
                                        "error", new Schema<>().type("string").example("Unauthorized"),
                                        "message", new Schema<>().type("string").example("JWT token missing or invalid"),
                                        "path", new Schema<>().type("string").example("/api/orders")
                                )))
                        .addSchemas("AuthenticationRequest",
                                new Schema<>().type("object").properties(Map.of(
                                        "email", new Schema<>().type("string").example("user@example.com"),
                                        "password", new Schema<>().type("string").example("secret123")
                                )))
                        .addSchemas("AuthenticationResponse",
                                new Schema<>().type("object").properties(Map.of(
                                        "token", new Schema<>().type("string").example("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                                )))
                );
    }
}