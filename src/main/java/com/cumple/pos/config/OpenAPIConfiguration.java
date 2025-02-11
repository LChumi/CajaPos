package com.cumple.pos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI expenseApi() {
        return new OpenAPI()
                .info(new Info().title("Importadora Cumpleaños Caja POS API Client")
                        .description("Api importadora cumpleaños Cliente Caja POS DataFast")
                        .version("1.0")
                        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }

    @Bean
    public GroupedOpenApi posApi() {
        return GroupedOpenApi.builder()
                .group("POS")
                .pathsToMatch("/pos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi clientStatus() {
        return GroupedOpenApi.builder()
                .group("Client-status")
                .pathsToMatch("/client/**")
                .build();
    }
}
