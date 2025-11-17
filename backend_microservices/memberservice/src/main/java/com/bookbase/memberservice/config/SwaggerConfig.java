package com.bookbase.memberservice.config;

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
                        new Info().title("bookbase APIs: Authentication & Member Service")
                                .description(
                                        "The official backend API for the bookbase application, designed to manage all core " +
                                        "user and authentication data. This specification includes: \n\n" +
                                        "1. **Authentication:** Secure endpoints for new user registration and existing user login/verification\n" +
                                        "2. **Members:** Full CRUD operations for managing user profiles, roles, and details (Admin/Member)"));
    }
}