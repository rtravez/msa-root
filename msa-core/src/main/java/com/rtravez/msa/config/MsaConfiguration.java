package com.rtravez.msa.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public RestClient restClientMcpServices() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
                        request.getHeaders().setBearerAuth(jwtAuthentication.getToken().getTokenValue());
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
