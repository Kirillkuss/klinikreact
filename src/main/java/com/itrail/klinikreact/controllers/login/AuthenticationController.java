package com.itrail.klinikreact.controllers.login;

import java.util.NoSuchElementException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.rest.login.IAuthentication;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class AuthenticationController implements IAuthentication {

    @GetMapping(value = "/login")
    public Mono<String> login() {
        return Mono.just( "login");
    }

    public Mono<String> index() {
        return Mono.just( "redirect:/index.html");
    }

    public Mono<String> changePassword() {
        return Mono.just( "change-password");
    }
    
    @ExceptionHandler(Throwable.class)
    public Mono<BaseError> errBaseResponse( Throwable ex ){
        log.info("Throwable");
        return Mono.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<BaseError> errBaseResponse( NoSuchElementException ex ){
        log.info("NoSuchElementException");
        return Mono.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<BaseError> errBaseResponse( IllegalArgumentException ex ){
        log.info("IllegalArgumentException");
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<BaseError> errBaseResponse( BadCredentialsException ex ){
        log.info("BadCredentialsException");
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }
}
