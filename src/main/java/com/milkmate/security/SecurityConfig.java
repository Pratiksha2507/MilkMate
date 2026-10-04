package com.milkmate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // PUBLIC PAGES
                // =========================

                .requestMatchers(
                    "/",
                    "/index.html",
                    "/*.html",
                    "/favicon.ico"
                ).permitAll()


                // =========================
                // STATIC FILES
                // =========================

                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()


                // =========================
                // AUTH
                // =========================

                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()


                // =========================
                // SWAGGER
                // =========================

                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()


                // =========================
                // ACTUATOR
                // =========================

                .requestMatchers(
                    "/actuator/health"
                ).permitAll()


                // =========================
                // ADMIN
                // =========================

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")


                // =========================
                // STAFF
                // =========================

                .requestMatchers(
                    "/api/staff/**"
                ).hasAnyRole(
                    "ADMIN",
                    "STAFF"
                )


                // =========================
                // FARMER
                // =========================

                .requestMatchers(
                    "/api/farmer/**"
                ).hasAnyRole(
                    "ADMIN",
                    "STAFF",
                    "FARMER"
                )


                // =========================
                // NOTIFICATIONS
                // =========================

                .requestMatchers(
                    "/api/notifications/**"
                ).hasAnyRole(
                    "ADMIN",
                    "STAFF",
                    "FARMER"
                )


                // =========================
                // EVERYTHING ELSE
                // =========================

                .anyRequest().authenticated()
            )


            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter
    jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter
                authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter
                .setAuthoritiesClaimName("role");

        authoritiesConverter
                .setAuthorityPrefix("ROLE_");


        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return converter;
    }
}