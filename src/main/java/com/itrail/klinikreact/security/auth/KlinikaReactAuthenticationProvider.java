package com.itrail.klinikreact.security.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Arrays;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KlinikaReactAuthenticationProvider implements ReactiveAuthenticationManager  {

    private final AuthService authService;
    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try{
           return authService.checkUser( authentication.getName(), authentication.getCredentials().toString() )
                .flatMap( foundUser -> {
                    return Mono.just( new UsernamePasswordAuthenticationToken( authentication.getName(),
                                                                               authentication.getCredentials().toString() ,
                                                                               Arrays.asList( new SimpleGrantedAuthority("ROLE_" + foundUser.getRole() ))));
                });
        }catch( AuthenticationException exception ){
            log.error( exception.getMessage() );
            return Mono.empty();
        }
    }
    
}
