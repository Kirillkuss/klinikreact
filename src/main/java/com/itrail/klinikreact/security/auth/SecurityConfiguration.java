package com.itrail.klinikreact.security.auth;

import java.net.URI;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
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
import com.itrail.klinikreact.security.handler.KlinikaReactLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final KlinikaReactAuthenticationFailureHandler klinikaReactAuthenticationFailureHandler;
    private final KlinikaReactAuthenticationSuccessHandler klinikaReactAuthenticationSuccessHandler;
    private final KlinikaReactLogoutSuccessHandler         klinikaReactLogoutSuccessHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(  "/login", "/change-password", "/react/logout", "/icon/", "/error").permitAll()
                .pathMatchers("/react/webjars/swagger-ui/index.html", "/react/klinikreact").hasAnyAuthority("ROLE_2", "2")
                .pathMatchers("/react/api/", "/react/", "/react/klinikreact", "/react/index", "/index.html", "/index", "/*.html")
                    .hasAnyAuthority("ROLE_0", "ROLE_1", "0", "1")
                .anyExchange().authenticated())
            .formLogin(formLogin -> formLogin
                .authenticationSuccessHandler(klinikaReactAuthenticationSuccessHandler)
                .authenticationFailureHandler(klinikaReactAuthenticationFailureHandler)
                .loginPage("/login"))
            .logout(logout -> logout.logoutUrl("/logout")
                                    .logoutSuccessHandler( klinikaReactLogoutSuccessHandler ))
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
