package com.bookbase.transactionservice.config;

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
                        new Info().title("bookbase APIs: Transaction Service")
                                .description(
                                        "The official backend API for the bookbase application, designed to manage all core " +
                                        "transaction data. This specification includes: \n\n" +
                                        "1. **Transactions:** All operations for managing transactions including creating, updating including PUT and PATCH methods"));
    }
}