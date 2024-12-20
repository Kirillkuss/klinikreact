package com.itrail.klinikreact.controllers.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import com.itrail.klinikreact.request.email.ChangePasswordRequest;
import com.itrail.klinikreact.rest.login.ISession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SessionCotroller implements ISession{

    private final UserSessionRepository userSessionRepository;

    @Override
    public Mono<Rendering> finishSession() {
        return Mono.just(Rendering.view("finish-session").build());
    }

    @Override
    public Mono<Rendering> deleteSession( ChangePasswordRequest changePasswordRequest, ServerWebExchange exchange ) {
        userSessionRepository.deleteById( changePasswordRequest.getUser());
        log.info( "Delete session success");
        if ( userSessionRepository.findById( changePasswordRequest.getUser()).isEmpty() ){
            return exchange.getSession().flatMap( s -> {
                System.out.println( "S >>> "+  s.getId() );
    
                return Mono.just( Rendering.view("finish-session")
                .modelAttribute("message", "Сессия была завершена!")
                .build());
            } );
        }else{
            return Mono.just( Rendering.view("finish-session")
                             .modelAttribute("error", "Что-то пошло не так!")
                             .build());

        }
    }
}
