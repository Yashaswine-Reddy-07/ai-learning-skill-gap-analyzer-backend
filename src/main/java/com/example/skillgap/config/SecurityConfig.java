package com.example.skillgap.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.skillgap.security.JwtAuthenticationFilter;

/**
 * Spring Security 6 configuration establishing stateless JWT filter chain and CORS policies.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${app.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the cryptographic PasswordEncoder bean using BCrypt.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager bean from AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the spring security config
     * @return AuthenticationManager instance
     * @throws Exception if manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception ex) {
            System.err.println("SecurityConfig: Error creating AuthenticationManager: " + ex.getMessage());
            logger.error("AuthenticationManager initialization failed", ex);
            throw ex;
        }
    }

    /**
     * Defines the HTTP Security Filter Chain with JWT stateless policy and endpoint protection rules.
     *
     * @param http the HttpSecurity builder
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/error").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/roles", "/api/roles/*", "/api/roles/*/quiz").permitAll()
        .anyRequest().authenticated()
)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception ex) {
            System.err.println("SecurityConfig: Error building SecurityFilterChain: " + ex.getMessage());
            logger.error("Security filter chain setup failure", ex);
            throw ex;
        }
    }

    /**
     * Centralized CORS configuration source resolving origins from environment variables.
     *
     * @return CorsConfigurationSource instance
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        try {
            CorsConfiguration configuration = new CorsConfiguration();
            List<String> origins = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .toList();

            configuration.setAllowedOrigins(origins);
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin"));
            configuration.setExposedHeaders(List.of("Authorization"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        } catch (Exception ex) {
            System.err.println("SecurityConfig: Error in corsConfigurationSource: " + ex.getMessage());
            logger.error("CORS configuration failed", ex);
            return new UrlBasedCorsConfigurationSource();
        }
    }
}
