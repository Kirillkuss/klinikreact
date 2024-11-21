package com.itrail.klinikreact.controllers.login;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.rest.login.IAuthentication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class AuthenticationController implements IAuthentication {

    @ExceptionHandler(Throwable.class)
    public Mono<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Mono.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<BaseError> errBaseResponse( NoSuchElementException ex ){
        return Mono.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<BaseError> errBaseResponse( IllegalArgumentException ex ){
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }

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
