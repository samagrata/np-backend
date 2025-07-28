package org.samagrata.npbackend.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurity {

  private final JwtFilter jwtFilter;
  private final JwtAuthEntryPoint unauthorizedHandler;

  public AppSecurity(
    JwtFilter jwtFilter,
    JwtAuthEntryPoint unauthorizedHandler
  ) {
    this.jwtFilter = jwtFilter;
    this.unauthorizedHandler = unauthorizedHandler;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
        .exceptionHandling(
          exception -> exception.authenticationEntryPoint(
            unauthorizedHandler)
        )
        .sessionManagement(
          session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
          .requestMatchers(
            HttpMethod.GET,
            "/api/*/stories/**",
            "/api/*/stories/*/comments"
          ).permitAll()
          .requestMatchers(
            HttpMethod.POST,
            "/api/*/auth/login",
            "/api/*/auth/register",
            "/api/*/volunteers",
            "/api/*/stories/*/comments"
          ).permitAll()
          .requestMatchers("/api/**").authenticated()
          .anyRequest().permitAll()
        );

    http.addFilterBefore(
      jwtFilter, 
      UsernamePasswordAuthenticationFilter.class
    );

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://127.0.0.1:4200")); // Allow your frontend origin(s)
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Allowed HTTP methods
    configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
    configuration.setAllowCredentials(true); // Allow sending credentials like cookies
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // Apply to all paths
    
    return source;
  }
}
