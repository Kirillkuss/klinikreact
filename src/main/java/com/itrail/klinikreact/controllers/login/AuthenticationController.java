package com.itrail.klinikreact.controllers.login;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.result.view.Rendering;
import com.itrail.klinikreact.rest.login.IAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthenticationController implements IAuthentication {

    @PreAuthorize("hasAnyRole('0', '1')")
    public Mono<Rendering> index() {
        return Mono.just( Rendering.view("redirect:/index.html").build());
    }

    @Override
    public Mono<Rendering> login() {
        return Mono.just( Rendering.view( "login").build());
    }

    @Override
    public Mono<Rendering> error() {
        return Mono.just( Rendering.view( "error").build());
    }

    @Override
    public Mono<String> clearErrorMessage(HttpServletRequest request) {
        request.getSession().removeAttribute("error");
        return Mono.just("redirect:/login"); 
    }
}
