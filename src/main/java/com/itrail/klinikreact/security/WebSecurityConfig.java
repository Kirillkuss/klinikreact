package com.itrail.klinikreact.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    //"/login", "/change-password"
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.exceptionHandling(handling -> handling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler((swe, e) -> 
                    Mono.fromRunnable(() -> swe.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN)))
            )
            .csrf(csrf -> csrf.disable())
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable())
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange(exchange -> exchange
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/login", "/change-password", "/**").permitAll() 
                .pathMatchers("/auth/", "/swagger-ui-custom.html", 
                              "/swagger-ui.html", "/swagger-ui/", 
                              "/v3/api-docs/", "/webjars/", 
                              "/swagger-ui/index.html", 
                              "/api-docs/", "/api/", "/").permitAll()
                .pathMatchers("/swagger/login").permitAll()
                .anyExchange().authenticated()
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}