package com.itrail.klinikreact.security.handler;
 
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class KlinikaReactAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler{

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        exchange.getAttributes().remove("error");
        
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        String redirectUrl;
        if (role.equals("ROLE_2")) {
            redirectUrl = "/webjars/swagger-ui/index.html";
        } else {
            redirectUrl = "/index";
        }
        // Установка статуса ответа и заголовка для перенаправления
        exchange.getResponse().setStatusCode(HttpStatus.FOUND); // 302 Found
        exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
        // Завершение обработки
        return exchange.getResponse().setComplete();
    }


}
