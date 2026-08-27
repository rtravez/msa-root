package com.rtravez.msa.auth;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiOpenAPI() {
		return new OpenAPI().info(new Info().title("Account Service API")
				.description("Account Service API Description").version("1.0"));
	}
}
