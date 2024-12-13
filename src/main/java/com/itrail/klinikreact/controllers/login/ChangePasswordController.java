package com.itrail.klinikreact.controllers.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import com.itrail.klinikreact.request.email.ChangePasswordRequest;
import com.itrail.klinikreact.rest.login.IChangePassword;
import com.itrail.klinikreact.services.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChangePasswordController implements IChangePassword{

    private final EmailService emailService;

    @Override
    public Mono<Rendering> changePassword() {
        return Mono.just(Rendering.view("change-password").build());
    }

    @Override
    public Mono<Rendering> requestPasswordChange( ChangePasswordRequest changePasswordRequest, ServerWebExchange exchange) {
        return emailService.sendNewPasswordToMail( changePasswordRequest.getUser() )
            .flatMap( t -> {
                return Mono.just( Rendering.view("change-password")
                                           .modelAttribute("message", "Новый пароль был отправлен на почту!")
                                           .build());
                }).onErrorResume(ex -> Mono.just(
                    Rendering.view("change-password")
                             .modelAttribute("error", ex.getMessage())
                             .build()
                ));    
    }
 
}
