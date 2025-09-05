package com.openclassrooms.rentals.Configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger/OpenAPI configuration class.
 *
 * This class sets up the OpenAPI specification for the application,
 * enabling Swagger UI to display and interact with the API endpoints.
 * It also defines the security scheme used for JWT-based authentication,
 * allowing developers to test protected routes directly from the Swagger interface.
 *
 * Key features:
 * <ul>
 *   <li>Defines API metadata such as title, version, and description</li>
 *   <li>Registers a bearer token security scheme for JWT authentication</li>
 *   <li>Applies the security requirement globally to all endpoints</li>
 * </ul>
 *
 * This configuration is essential for generating interactive API documentation
 * and facilitating development and testing workflows.
 *
 * @author Pagès
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI specification for the application.
     *
     * @return an {@link OpenAPI} instance containing metadata and security definitions
     */
    @Bean
    public OpenAPI apiCfg() {
        return new OpenAPI()
            .info(new Info()
                .title("Estate API")
                .version("1.0")
                .description("API documentation for estate by Chatop"))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes("Bearer Authentication",
                new SecurityScheme()
                    .name("Bearer Authentication")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}