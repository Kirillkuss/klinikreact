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
        return exchange.getSession().flatMap(session -> {
            session.getAttributes().put("error", exception.getMessage());
            String redirectUrl = UriComponentsBuilder.fromPath("/react/login")
                    .queryParam("error", "true")
                    .toUriString();
            exchange.getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
            return exchange.getResponse().setComplete();
        });
    }
}
