package com.itrail.klinikreact.controllers.login;

import org.springframework.stereotype.Controller;
import com.itrail.klinikreact.rest.login.IAuthentication;
import reactor.core.publisher.Mono;

@Controller
public class AuthenticationController implements IAuthentication {

    public Mono<String> login() {
        return Mono.just( "login");
    }

    public Mono<String> index() {
        return Mono.just( "redirect:/index.html");
    }

    public Mono<String> changePassword() {
        return Mono.just( "change-password");
    }
    
}
