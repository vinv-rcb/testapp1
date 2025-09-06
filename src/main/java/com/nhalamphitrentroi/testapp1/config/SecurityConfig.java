package com.nhalamphitrentroi.testapp1.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Completely disable security for register endpoint
                .requestMatchers(HttpMethod.POST, "/sqlanalys/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/sqlanalys/register").permitAll()
                .requestMatchers("/sqlanalys/register/**").permitAll()
                
                // Allow other endpoints
                .requestMatchers("/sqlanalys/login").permitAll()
                .requestMatchers("/sqlanalys/test").permitAll()
                .requestMatchers("/sqlanalys/health").permitAll()
                
                // Allow sqlanalys API endpoints (they handle their own token validation)
                .requestMatchers("/sqlanalys/database").permitAll()
                .requestMatchers("/sqlanalys/log").permitAll()
                
                // Allow admin endpoints (for now, in production you'd want proper authentication)
                .requestMatchers("/sqlanalys/admin/**").permitAll()
                
                // Allow sqlanalys endpoints (they handle their own token validation)
                .requestMatchers("/api/sqlanalys/**").permitAll()
                .requestMatchers("/api/logs/**").permitAll()
                .requestMatchers("/api/verification/**").permitAll()
                
                // Allow H2 console and actuator
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                
                // Allow all OPTIONS requests for CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }
}
