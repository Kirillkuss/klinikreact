package com.itrail.klinikreact.controllers.login;

import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.request.login.AuthRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.login.AuthResponse;
import com.itrail.klinikreact.rest.login.IAuthenticationSwagger;
import com.itrail.klinikreact.security.JWTUtil;
import com.itrail.klinikreact.services.model.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationControlller implements IAuthenticationSwagger {

    private final JWTUtil jwtUtil;
    private final UserService userService; 

    @ExceptionHandler(Throwable.class)
    public Flux<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Flux.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Flux<BaseError> errBaseResponse( NoSuchElementException ex ){
        ex.printStackTrace();
        return Flux.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Flux<BaseError> errBaseResponse( IllegalArgumentException ex ){
        ex.printStackTrace();
        return Flux.just( new BaseError( 404, ex.getMessage() ));
    }

    public Mono<ResponseEntity<AuthResponse>> login( AuthRequest authRequest ) {
        return userService.findByLogin( authRequest.getUsername())
                           .filter( filter -> userService.checkUserPassword( authRequest.getPassword(), filter.getSalt(), filter.getPassword() ))
                                .map( users -> ResponseEntity.status( HttpStatus.OK )
                                   // .header( "X-AUTH-TOKEN", jwtUtil.generateToken( users ))
                                    .body( new AuthResponse( 200, jwtUtil.generateToken( users ))))
                                    .switchIfEmpty( Mono.just( ResponseEntity.status( HttpStatus.UNAUTHORIZED )
                                    .body( new AuthResponse( 401, "Invalid login or password" ))));
    }

}