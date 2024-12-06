package com.itrail.klinikreact.security.auth;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.itrail.klinikreact.security.handler.KlinikaReactAuthenticationFailureHandler;
import com.itrail.klinikreact.security.handler.KlinikaReactAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final KlinikaReactAuthenticationFailureHandler klinikaReactAuthenticationFailureHandler;
    private final KlinikaReactAuthenticationSuccessHandler klinikaReactAuthenticationSuccessHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/login", "/change-password", "/logout", "/icon/", "/error").permitAll()
                .pathMatchers("/react/webjars/swagger-ui/index.html").hasAuthority("ROLE_2")
                .pathMatchers("/react/api/", "/react/", "/react/klinikreact", "/react/index", "/index.html", "/index")
                    .hasAnyAuthority("ROLE_0", "ROLE_1", "0", "1")
                .anyExchange().authenticated())
            .formLogin(formLogin -> formLogin
                .authenticationSuccessHandler(klinikaReactAuthenticationSuccessHandler)
                .authenticationFailureHandler(klinikaReactAuthenticationFailureHandler)
                .loginPage("/login"))
            .logout(logout -> logout.logoutUrl("/logout"))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exceptionHandling -> 
                exceptionHandling.accessDeniedHandler((exchange, denied) -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create("/react/error"));
                    return response.setComplete();
                }))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

} 
