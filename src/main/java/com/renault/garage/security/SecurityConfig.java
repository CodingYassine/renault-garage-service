package com.renault. garage.security;

import org. springframework.context.annotation.Bean;
import org.springframework.context. annotation.Configuration;
import org. springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework. security.config.annotation.web. builders.HttpSecurity;
import org.springframework.security.config. annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security. oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // ✅ Chaîne 1 : Actuator endpoints (PUBLIC, sans sécurité)
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests(auth -> auth. anyRequest().permitAll());
        return http.build();
    }

    // ✅ Chaîne 2 : API endpoints (protégés par OAuth2 + rôles)
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/garages/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/vehicles/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/accessories/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/search/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }
}