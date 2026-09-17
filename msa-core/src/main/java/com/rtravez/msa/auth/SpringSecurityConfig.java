package com.rtravez.msa.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig {

	private static final String KEYCLOAK_CLIENT_ID = "MSA-WS";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ACCOUNTS_API_PATH = "/api/accounts/**";
	private static final String MOVEMENTS_API_PATH = "/api/movements/**";
	private static final String[] PUBLIC_ENDPOINTS = {
			"/error",
			"/v3/api-docs/**",
			"/swagger-ui/**",
			"/actuator/health",
			"/actuator/info",
			"/actuator/health/**"
	};

	@Bean
	@Order(1)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
				.requestMatchers(HttpMethod.GET, ACCOUNTS_API_PATH).hasAuthority(ROLE_ADMIN)
				.requestMatchers(HttpMethod.POST, ACCOUNTS_API_PATH).hasAuthority(ROLE_ADMIN)
				.requestMatchers(HttpMethod.PUT, ACCOUNTS_API_PATH).hasAuthority(ROLE_ADMIN)
				.requestMatchers(HttpMethod.DELETE, ACCOUNTS_API_PATH).hasAuthority(ROLE_ADMIN)
				.requestMatchers(HttpMethod.GET, MOVEMENTS_API_PATH).hasAuthority(ROLE_ADMIN)
				.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
		authenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakAuthoritiesConverter());
		return authenticationConverter;
	}

	private Converter<Jwt, Collection<GrantedAuthority>> keycloakAuthoritiesConverter() {
		return jwt -> {
			Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
			Map<String, Object> clientAccess = resourceAccess == null
					? null
					: asMap(resourceAccess.get(KEYCLOAK_CLIENT_ID));

			return Stream.concat(
					roleNames(jwt.getClaimAsMap("realm_access")),
					roleNames(clientAccess))
					.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
					.map(SimpleGrantedAuthority::new)
					.map(GrantedAuthority.class::cast)
					.toList();
		};
	}

	private Stream<String> roleNames(Map<String, Object> access) {
		if (access == null || !(access.get("roles") instanceof Collection<?> roles)) {
			return Stream.empty();
		}

		return roles.stream()
				.filter(String.class::isInstance)
				.map(String.class::cast);
	}

	private Map<String, Object> asMap(Object value) {
		if (!(value instanceof Map<?, ?> map)) {
			return Map.of();
		}

		Map<String, Object> typedMap = new HashMap<>();
		map.forEach((key, entryValue) -> {
			if (key instanceof String stringKey) {
				typedMap.put(stringKey, entryValue);
			}
		});
		return typedMap;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}