package com.example.ums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration – HTTP Basic auth with role-based access control.
 *
 * Access rules:
 *   /api/admins/**    → ADMIN only
 *   /api/teachers/**  → ADMIN or TEACHER
 *   /api/students/**  → ADMIN or STUDENT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // allow H2 console
            .authorizeHttpRequests(auth -> auth
                // Static frontend & H2 console
                .requestMatchers("/", "/index.html", "/h2-console/**").permitAll()
                // Admin endpoints – only ADMIN
                .requestMatchers("/api/admins/**").hasRole("ADMIN")
                // Teacher endpoints – ADMIN or TEACHER
                .requestMatchers("/api/teachers/**").hasAnyRole("ADMIN", "TEACHER")
                // Student endpoints – ADMIN or STUDENT
                .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "STUDENT")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
            );

        return http.build();
    }
}

