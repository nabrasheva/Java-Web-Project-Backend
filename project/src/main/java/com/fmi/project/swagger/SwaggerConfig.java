package com.fmi.project.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI notificationsOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Event manager API")
            .description("University project which implements event manager application")
            .version("V0.0.1"));
  }
}