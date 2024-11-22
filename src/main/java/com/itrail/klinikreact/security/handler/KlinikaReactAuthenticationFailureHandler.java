package com.itrail.klinikreact.security.handler;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KlinikaReactAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        // Логирование ошибки аутентификации
        log.error("Authentication failed: {}", exception.getMessage());
        // Установка сообщения об ошибке в атрибутах обмена
        exchange.getAttributes().put("error", exception.getMessage());
        // Подготовка перенаправления на страницу входа с параметром `error=true`
        String redirectUrl = UriComponentsBuilder.fromPath("/login").queryParam("error", "true").toUriString();
        // Установка статуса ответа и заголовка Location для перенаправления
        exchange.getResponse().setStatusCode(HttpStatus.FOUND); // 302 Found
        exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
        // Возврат Mono<Void> для завершения обработки
        return exchange.getResponse().setComplete();
    }
  
}
