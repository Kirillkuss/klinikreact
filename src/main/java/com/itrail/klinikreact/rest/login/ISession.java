package com.itrail.klinikreact.rest.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.request.email.ChangePasswordRequest;
import reactor.core.publisher.Mono;

public interface ISession {

    @GetMapping("/finish-session")
    public Mono<Rendering> finishSession();

    @PostMapping("finish-session")
    public Mono<Rendering> deleteSession( @ModelAttribute ChangePasswordRequest changePasswordRequest, ServerWebExchange exchange );
    
}
