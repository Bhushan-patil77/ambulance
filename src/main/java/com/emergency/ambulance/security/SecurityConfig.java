package com.emergency.ambulance.security;

import com.emergency.ambulance.admin.service.AdminUserDetailsService;
import com.emergency.ambulance.ambulance.service.AmbulanceUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AmbulanceUserDetailsService ambulanceUserDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(adminAuthenticationProvider())
            .authenticationProvider(ambulanceAuthenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/v3/api-docs/**", // Allow access to OpenAPI 3 documentation
                        "/swagger-ui/**",
                        "/swagger-ui.html", // Specific HTML file for Swagger UI
                        "/swagger-ui/index.html", // Common entry point for Swagger UI
                        "/api/admins/login",
                        "/api/ambulances/login",
                        "/drivers/login",
                        "/drivers/assignDriver"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationProvider ambulanceAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(ambulanceUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(adminAuthenticationProvider(), ambulanceAuthenticationProvider());
    }
}
