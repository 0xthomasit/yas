package com.yas.product.config;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // 🔐 AUTHORIZATION RULES
                .authorizeHttpRequests(auth -> auth
                        // ✅ PUBLIC ENDPOINTS (no authentication required)
                        .requestMatchers("/actuator/prometheus", // Metrics endpoint
                                "/actuator/health/**", // Health check
                                "/swagger-ui", "/swagger-ui/**", // API documentation
                                "/error",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ✅ STOREFRONT ENDPOINTS (public access)
                        .requestMatchers("/storefront/**").permitAll()

                        // 🔒 BACKOFFICE ENDPOINTS (admin only)
                        .requestMatchers("/backoffice/**").hasRole("ADMIN")

                        // 🔒 ALL OTHER ENDPOINTS (authenticated users)
                        .anyRequest().authenticated()
                )
                // 🎫 USE OAUTH2 JWT TOKENS from Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        // 🎯 CONVERT KEYCLOAK ROLES TO SPRING SECURITY ROLES
        // Keycloak JWT contains: {"realm_access": {"roles": ["ADMIN", "USER"]}}
        // This converter extracts roles and adds "ROLE_" prefix
        // Result: ROLE_ADMIN, ROLE_USER
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> roles = realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
