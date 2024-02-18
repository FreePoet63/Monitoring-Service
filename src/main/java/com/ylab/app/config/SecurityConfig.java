package com.ylab.app.config;

import com.ylab.app.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

/**
 * SecurityConfig class represents the configuration class for setting up the security configuration of the application.
 *
 * This class includes configuration for authentication provider, authentication manager, filter chain, and password encoder.
 *
 * @author razlivinsky
 * @since 14.02.2024
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userService;

    public SecurityConfig(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Configures and provides the DaoAuthenticationProvider for authentication.
     *
     * @return the configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configures and provides the AuthenticationManager.
     *
     * @return the configured AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    /**
     * Configures and provides the security filter chain.
     *
     * @param http the HttpSecurity to be configured
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> {
                    try {
                        authz
                                .requestMatchers("/login*", "/users/*")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/register")
                                .permitAll()
                                .anyRequest()
                                .authenticated();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and()
                .csrf()
                .disable()
                .sessionManagement();
        return http.build();
    }

    /**
     * Configures and provides the password encoder for encrypting and verifying passwords using BCrypt hashing algorithm.
     *
     * @return the configured BCryptPasswordEncoder for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}