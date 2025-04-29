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

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;
import java.util.List;

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
            // .cors(cors -> cors.disable())
            .cors(withDefaults())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                                   .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                   /*
                                     .requestMatchers(HttpMethod.POST, "/auth/register").hasRole(Role.ADMIN)
                                     .requestMatchers("/auth/register").denyAll()
                                   */
                                   .requestMatchers("/participants/**").hasAnyRole(Role.USER, Role.ADMIN)
                                   .requestMatchers("/training_sessions/**").hasAnyRole(Role.USER, Role.ADMIN)
                                   .requestMatchers("/trainers/**").hasAnyRole(Role.USER, Role.ADMIN)

                                   .requestMatchers("/users/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/profiles/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/structures/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/domains/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/roles/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/employers/**").hasRole(Role.ADMIN)
                                   .requestMatchers("/dashboard/**").hasAnyRole(Role.RESPONSIBLE, Role.ADMIN)
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
