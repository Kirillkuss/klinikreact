package com.itrail.klinikreact.controllers.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;


@Controller
public class SessionCotroller {

    @GetMapping("/finish-session")
    public Mono<Rendering> finishSession() {
        return Mono.just(Rendering.view("finish-session").build());
    }
}
