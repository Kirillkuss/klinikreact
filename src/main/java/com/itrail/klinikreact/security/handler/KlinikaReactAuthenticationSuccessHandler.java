package com.itrail.klinikreact.security.handler;
 
import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.redis.model.UserSession;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KlinikaReactAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler{

    private final UserSessionRepository userSessionRepository;

    @PostConstruct
    public void init(){
        /**
         * Delete all session users
         */
        log.info(" Delete all session! ");
        userSessionRepository.deleteAll();
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        exchange.getAttributes().remove("error");
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        String redirectUrl = role.equals("ROLE_2") ? "/react/webjars/swagger-ui/index.html" : "/react/index";

        exchange.getResponse().setStatusCode( HttpStatus.FOUND ); 
        exchange.getResponse().getHeaders().setLocation( URI.create( redirectUrl ));
        return exchange.getSession().flatMap( session -> {
            return Mono.defer( () -> {
                if( userSessionRepository.findById(  authentication.getName() ).isEmpty()){
                    userSessionRepository.save( new UserSession( authentication.getName(), session.getId(), 60 ));
                    log.info("SAVE session -> " + session.getId() );
                    return exchange.getResponse().setComplete();
                 }else{
                    exchange.getResponse().setStatusCode( HttpStatus.FOUND );
                    exchange.getResponse().getHeaders().setLocation( URI.create( "/react/finish-session" ));
                    return exchange.getResponse().setComplete();
                 }
            });
        });
    }

}
