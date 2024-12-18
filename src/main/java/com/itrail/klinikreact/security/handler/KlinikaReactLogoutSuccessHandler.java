package com.itrail.klinikreact.security.handler;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KlinikaReactLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final UserSessionRepository userSessionRepository;

    @Override
    public Mono<Void> onLogoutSuccess( WebFilterExchange webFilterExchange, Authentication authentication ) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        userSessionRepository.deleteById( authentication.getName());
       // log.info( "Delete session for user: " + authentication.getName() );
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create("/react/login")); 
        return exchange.getResponse().setComplete();
    }
}
