package com.bookbase.notificationservice.config;

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
                        new Info().title("bookbase APIs: Notification Service")
                                .description(
                                        "The official backend API for the bookbase application, designed to manage all core " +
                                        "notification data. This specification includes: \n\n" +
                                        "1. **Notifications:** Full CRUD operations for managing notifications including adding, updating and deletion"));
    }
}