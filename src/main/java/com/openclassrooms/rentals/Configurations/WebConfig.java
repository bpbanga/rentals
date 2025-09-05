package com.openclassrooms.rentals.Configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for resource handling and CORS setup.
 *
 * This class customizes Spring MVC behavior by:
 * <ul>
 *   <li>Mapping URL paths to local file system directories for serving static resources</li>
 *   <li>Configuring Cross-Origin Resource Sharing (CORS) to allow frontend access from Angular</li>
 * </ul>
 *
 * It ensures that uploaded rental pictures can be accessed via HTTP,
 * and that the frontend application running on <code>http://localhost:4200</code>
 * can interact with the backend without CORS restrictions.
 *
 * @author Pagès
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Maps HTTP requests to local file system paths.
     * <p>
     * This method allows files stored in the <code>uploads/</code> directory
     * to be served via the endpoint <code>/api/files/rentalpicture/**</code>.
     *
     * @param registry the resource handler registry used to define mappings
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/files/rentalpicture/**")
                .addResourceLocations("file:uploads/");
    }

    /**
     * Configures CORS settings to allow requests from the Angular frontend.
     * <p>
     * This bean enables cross-origin requests from <code>http://localhost:4200</code>,
     * supporting common HTTP methods and headers, and allowing credentials.
     *
     * @return a {@link WebMvcConfigurer} instance with CORS mappings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}