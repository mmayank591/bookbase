package com.bookbase.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition(tags = {
        @Tag(name = "Authentication API", description = "Endpoints for User Registration and User Login"),
        @Tag(name = "Members API", description = "CRUD operation for Members"),
        @Tag(name = "Books API", description = "CRUD operation for Books"),
        @Tag(name = "Transactions API", description = "CRUD operation for Transactions"),
        @Tag(name = "Notification API", description = "CRUD operation for Notifications"),
        @Tag(name = "Fines API", description = "CRUD operation for Fines")
})

public class SwaggerConfig {

    @Bean
    public OpenAPI myConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("bookbase APIs")
                                .description(
                                        "The official backend API for the Bookbase application, designed to manage all core " +
                                        "user and authentication data. This specification includes: \n\n" +
                                        "1. **Authentication:** Secure endpoints for new user registration and existing user login/verification. \n" +
                                        "2. **Members:** Full CRUD operations for managing user profiles, roles, and details (Admin/Member)."));
    }
}