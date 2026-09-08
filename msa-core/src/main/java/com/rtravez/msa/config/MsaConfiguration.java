package com.rtravez.msa.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

/**
 * MsaConfiguration spring configuration.
 *
 * @author renetravez
 * @version 1.0
 */
@EnableAsync
@EnableJpaRepositories(basePackages = { "com.rtravez.msa.repository" }, repositoryImplementationPostfix = "Impl")
public class MsaConfiguration {

    @Bean
    public RestClient mscServices(@Value("${msc-service.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(Objects.requireNonNull(baseUrl))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor((request, body, execution) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
                        request.getHeaders()
                                .setBearerAuth(Objects.requireNonNull(jwtAuthentication.getToken().getTokenValue()));
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
