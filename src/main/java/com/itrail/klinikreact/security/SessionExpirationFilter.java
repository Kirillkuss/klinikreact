package com.itrail.klinikreact.security;

import java.net.URI;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.itrail.klinikreact.redis.model.UserSession;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionExpirationFilter implements WebFilter {


    @Override
    public Mono<Void> filter( ServerWebExchange exchange, WebFilterChain chain ) {
       // String value = exchange.getRequest().getCookies().get("SESSION").iterator().next().getValue();
        //System.out.println( "value >>" + value );
        return chain.filter(exchange);
    }
}
