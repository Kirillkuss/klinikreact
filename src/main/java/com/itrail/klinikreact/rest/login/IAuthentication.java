package com.itrail.klinikreact.rest.login;

import reactor.core.publisher.Mono;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;

public interface IAuthentication {

    @GetMapping(value = "/index")
    public Mono<Rendering> index();

    @GetMapping(value = "/login")
    public Mono<Rendering> login();

    @PostMapping(value = "clear-error-message", produces = MediaType.APPLICATION_JSON)
    public Mono<String> clearErrorMessage(HttpServletRequest request);

    @GetMapping( value = "/error")
    public Mono<Rendering> error();

}
