package com.itrail.klinikreact.rest.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.request.email.ChangePasswordRequest;
import reactor.core.publisher.Mono;

public interface IChangePassword {
    
    @GetMapping(value = "/change-password")
    public Mono<Rendering> changePassword();

    @PostMapping(value = "change-password" )
    public Mono<Rendering> requestPasswordChange ( @ModelAttribute ChangePasswordRequest changePasswordRequest, ServerWebExchange exchange );
}
