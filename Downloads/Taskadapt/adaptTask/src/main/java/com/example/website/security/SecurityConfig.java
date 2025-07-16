package com.example.website.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ---------- PUBLIC: Auth + H2 Console ----------
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

                // ---------- PUBLIC: Static Resources ----------
                .requestMatchers(
                    "/index.html",
                    "/",
                    "/favicon.ico",
                    "/js/**",
                    "/css/**",
                    "/images/**"
                ).permitAll()

                // ---------- PUBLIC: View Products ----------
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // ---------- ADMIN: Manage Products ----------
                .requestMatchers(HttpMethod.POST,   "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                // ---------- CUSTOMER: Cart ----------
                .requestMatchers(HttpMethod.GET,    "/api/cart/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.POST,   "/api/cart/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/api/cart/**").hasRole("CUSTOMER")

                // ---------- CUSTOMER: Orders ----------
                .requestMatchers(HttpMethod.GET,    "/api/orders/**").hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.POST,   "/api/orders/**").hasRole("CUSTOMER")

                // ---------- Everything Else ----------
                .anyRequest().authenticated()
            )
            // Allow frames (for H2 console)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // 🔐 Add JWT Filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
