package com.ravi.waterlilly.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    // Configures the security filter chain for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with custom configuration
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // Allow preflight requests for CORS
                        .requestMatchers("/ws/**", "/topic/**", "/app/**", "/queue/**").permitAll() // WebSocket endpoints
                        .requestMatchers("/api/auth/**").permitAll() // Allow public access to authentication endpoints
                        .requestMatchers("/api/employee/**").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers("/api/users/**").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers("/api/privilege/**").hasAuthority("Admin") // Restrict access only to Admin
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions only if required
                )
                .authenticationProvider(authenticationProvider()) // Set the authentication provider
                .formLogin(form -> form.disable()) // Disable form-based login
                .httpBasic(basic -> basic.disable()) // Disable HTTP Basic authentication
                .build();
    }

    // Configures CORS (Cross-Origin Resource Sharing) settings
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from these origins
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://bit-pro-ravi.netlify.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        configuration.setMaxAge(3600L); // Cache preflight responses for 1 hour

        // Register the CORS configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean to configure the authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder()); // Use BCrypt for password hashing
        provider.setUserDetailsService(userDetailsService); // Set the custom UserDetailsService
        return provider;
    }

    // Bean to expose the AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean to provide the BCryptPasswordEncoder for password hashing
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}