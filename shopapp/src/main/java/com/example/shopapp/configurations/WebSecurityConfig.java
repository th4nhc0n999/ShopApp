package com.example.shopapp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.models.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers("/api/v1/users/register", "/api/v1/users/login", "/error").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**", "/api/v1/categories").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/categories/**", "/api/v1/categories").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**", "/api/v1/categories").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**", "/api/v1/categories").hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET, "/api/v1/products/**", "/api/v1/products").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/products/**", "/api/v1/products").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**", "/api/v1/products").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**", "/api/v1/products").hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasRole(Role.USER)
                            .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.POST, "/api/v1/order_details/**").hasRole(Role.USER)
                            .requestMatchers(HttpMethod.GET, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/v1/order_details/**").hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/order_details/**").hasRole(Role.ADMIN)

                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
