package org.green_building.excellent_training.security;

import org.green_building.excellent_training.entities.Role;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                                   .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                   .requestMatchers("/auth/login").denyAll()
                                   /*
                                   .requestMatchers(HttpMethod.POST, "/auth/register").hasRole(Role.ADMIN)
                                   .requestMatchers("/auth/register").denyAll()
                                   */
                                   .requestMatchers("/users/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/roles/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/profiles/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/structures/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/participants/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/domains/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/training_sessions/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/employers/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/trainers/**").hasRole(Role.ADMIN)
                                   .anyRequest().denyAll()
                                   )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
