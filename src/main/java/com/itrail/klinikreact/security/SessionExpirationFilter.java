package com.itrail.klinikreact.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
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
