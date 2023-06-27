package com.fmi.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI notificationsOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Event manager API")
            .description("University project")
            .version("v0.0.1"));
  }
}