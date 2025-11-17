package com.bookbase.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    @Primary
    public OpenAPI myConfig() {
        return new OpenAPI()
            .info(new Info().title("bookbase API Gateway Documentation")
                .description("Aggregated documentation for all Microservices"));
    }
}