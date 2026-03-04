package com.procurement.procurement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth
                                                // Public
                                                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()

                                                // Role based access
                                                .requestMatchers("/api/vendor", "/api/vendor/**", "/vendor/**")
                                                .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER", "EMPLOYEE")

                                                .requestMatchers("/api/procurement/requisition",
                                                                "/api/procurement/requisition/**", "/requisition/**")
                                                .hasAnyRole("ADMIN", "EMPLOYEE", "PROCUREMENT_MANAGER")

                                                .requestMatchers("/api/procurement/purchase-order",
                                                                "/api/procurement/purchase-order/**",
                                                                "/purchase-order/**")
                                                .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                                                .requestMatchers("/api/procurement/approval",
                                                                "/api/procurement/approval/**", "/approval/**")
                                                .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                                                .requestMatchers("/api/procurement/stats").authenticated()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}