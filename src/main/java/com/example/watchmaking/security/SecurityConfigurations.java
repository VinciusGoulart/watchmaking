package com.example.watchmaking.security;

import com.example.watchmaking.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfigurations {
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(
                        AbstractHttpConfigurer::disable
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(authorized -> authorized
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/persons/**").authenticated()
                        .requestMatchers("/service-orders/**").authenticated()
                        .requestMatchers("/users/updatePassword/**").authenticated()

                        .requestMatchers("/users/**").hasRole("100")
                        .requestMatchers("/storage/**").hasAnyRole("100", "102")
                        .requestMatchers("/watches/**").hasAnyRole("100", "102")
                        .requestMatchers("/watch-types/**").hasAnyRole("100", "102")
                        .requestMatchers("/watch-parts/**").hasAnyRole("100", "102")
                        .requestMatchers("/watch-categories/**").hasAnyRole("100", "102")
                        .requestMatchers("/brands/**").hasAnyRole("100", "102")
                        .anyRequest().authenticated()
                ).httpBasic(
                        httpBasic -> {
                        }
                ).addFilterBefore(
                        securityFilter, UsernamePasswordAuthenticationFilter.class
                )
                .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authorizationService); // seu CustomUserDetailsService
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
