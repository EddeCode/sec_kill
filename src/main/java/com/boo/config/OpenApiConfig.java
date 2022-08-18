package com.boo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author song
 * swagger的相关配置
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI initOpenAPI() {
        return new OpenAPI().info(
                new Info().title("MULTIVERSE SHOP API").description("OpenAPI").version("v1.0")
        );
    }
}