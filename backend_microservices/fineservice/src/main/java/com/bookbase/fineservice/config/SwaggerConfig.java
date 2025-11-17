package com.bookbase.fineservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("bookbase APIs: Fine Service")
                                .description(
                                        "The official backend API for the bookbase application, designed to manage all core " +
                                        "fine data. This specification includes: \n\n" +
                                        "1. **Fines:** All operations for managing fines including adding and updating using PUT and PATCH methods"));
    }
}