package com.itrail.klinikreact.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    
    private final JWTUtil jwtUtil;
    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try{
            String authToken = authentication.getCredentials().toString();
            String username = jwtUtil.getUsernameFromToken(authToken);
            return Mono.just(jwtUtil.validateToken( authToken ))
                       .filter( valid -> valid )
                            .switchIfEmpty( Mono.empty() )
                            .map( valid -> {
                                Claims claims = jwtUtil.getAllClaimsFromToken( authToken );
                                return new UsernamePasswordAuthenticationToken( username, null, Arrays.asList( claims.get( "role", String.class ))
                                                                                                      .stream()
                                                                                                      .map(SimpleGrantedAuthority::new)
                                                                                                      .collect(Collectors.toList()));
                                });
        }catch( ExpiredJwtException ex ){
            log.error( ex.getMessage() );
            return Mono.empty();
        }
    }
}