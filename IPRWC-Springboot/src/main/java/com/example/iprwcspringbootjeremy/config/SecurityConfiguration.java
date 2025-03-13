package com.example.iprwcspringbootjeremy.config;

import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.*;
import org.springframework.security.config.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> corsConfigurer() {
        return new CorsConfigurer<>();
    }

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(GET, "/api/v1/product/**").permitAll()
                        .requestMatchers(POST, "/api/v1/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(PUT, "/api/v1/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(GET, "/api/v1/category/**").permitAll()
                        .requestMatchers(POST, "/api/v1/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(PUT, "/api/v1/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(GET, "/api/v1/user/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(GET, "/api/v1/user/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(PUT, "/api/v1/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(DELETE, "/api/v1/user/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(GET, "/api/v1/order/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(POST, "/api/v1/order/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
