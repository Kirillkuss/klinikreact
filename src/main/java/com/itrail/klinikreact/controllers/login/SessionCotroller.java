package com.itrail.klinikreact.controllers.login;

import java.util.Optional;
import java.util.Map.Entry;
import java.util.List;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.redis.model.UserSession;
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

    private final ReactiveRedisTemplate<String, UserSession> reactiveRedisTemplate;
    private final UserSessionRepository userSessionRepository;

    @Override
    public Mono<Rendering> finishSession() {
        return Mono.just(Rendering.view("finish-session").build());
    }

    @Override
    public Mono<Rendering> deleteSession( ChangePasswordRequest changePasswordRequest, ServerWebExchange exchange ) {
        String user = exchange.getRequest()
                              .getCookies()
                              .get( "name")
                              .get(0 )
                              .getValue();
        deleteCookies( exchange );
        Optional<UserSession> session =  userSessionRepository.findById( user );
        if ( session.isPresent() ){
            userSessionRepository.deleteById( user );
            return reactiveRedisTemplate.delete( "spring:session:sessions:" + session.orElse(null ).getSessionId() )
                .flatMap( del ->{
                    return Mono.just( Rendering.view("finish-session")
                    .modelAttribute("message", "Сессия была завершена!")
                    .build());
                });
        }else{
            return Mono.just( Rendering.view("finish-session")
                             .modelAttribute("error", "Что-то пошло не так!")
                             .build());
        }
    }

    private void deleteCookies( ServerWebExchange exchange ){
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        for ( Entry<String, List<HttpCookie>> cookie : cookies.entrySet()) {
            for ( HttpCookie cookieToBeDeleted : cookie.getValue()) {
                exchange.getResponse()
                        .addCookie( ResponseCookie.from( cookieToBeDeleted.getName(), cookieToBeDeleted.getValue() )
                        .maxAge(0)
                        .build() );
            }
        }
    }
}
