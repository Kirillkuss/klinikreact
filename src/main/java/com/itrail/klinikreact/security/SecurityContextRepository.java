package com.itrail.klinikreact.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.justOrEmpty( swe.getRequest()
                                    .getHeaders()
                                    .getFirst( HttpHeaders.AUTHORIZATION ))
                   .filter(authHeader -> authHeader.startsWith("Bearer "))
                   .flatMap(authHeader -> {
                        return this.authenticationManager
                                   .authenticate( new UsernamePasswordAuthenticationToken(authHeader.substring(7), authHeader.substring(7)))
                                   .map(SecurityContextImpl::new);
                    });
    }
}