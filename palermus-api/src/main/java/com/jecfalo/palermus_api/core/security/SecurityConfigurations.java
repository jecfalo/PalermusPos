package com.jecfalo.palermus_api.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;
    @Autowired
    RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                            // --- Endpoints públicos (sin autenticación) ---
                            auth.requestMatchers(HttpMethod.POST, "/api/users").permitAll();
                            auth.requestMatchers(HttpMethod.POST,"/api/login").permitAll();
                            auth.requestMatchers(HttpMethod.POST,"/api/refresh").permitAll();
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                            // --- Swagger: solo accesible para administradores ---
                            auth.requestMatchers("/v3/api-docs/**",
                                    "/swagger-ui.html",
                                    "/swagger-ui/**").permitAll();
                            //.hasRole("ADMIN");
                            // --- Endpoints protegidos por rol ---
                            auth.requestMatchers("/api/storages-room/**", "/api/users/**", "/api/profiles/**").hasRole("ADMIN");
                            auth.requestMatchers("/api/products/**", "/api/crud-inv/**", "/api/orders/**", "/api/cash-registers/**").hasAnyRole("ADMIN", "SELLER");

                            // --- Cualquier otra petición requiere autenticación ---
                            auth.anyRequest().authenticated();
                        }
                )
                .addFilterBefore(rateLimitFilter, LogoutFilter.class)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    public PasswordEncoder passwordEncoder(){
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        /*
         * Si en algún momento necesitas configurarlo manualmente (ej. porque tu servidor
         * tiene muy poca memoria RAM), puedes instanciarlo con parámetros personalizados:
         *
         * int saltLength = 16;      // Tamaño de la sal
         * int hashLength = 32;      // Tamaño del hash resultante
         * int parallelism = 1;      // Hilos de CPU a usar (1 para servidores pequeños)
         * int memory = 16384;       // Memoria en KB (16MB) - ¡La clave de su seguridad!
         * int iterations = 2;       // Vueltas de procesamiento
         *
         * return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
         */
    }

}
