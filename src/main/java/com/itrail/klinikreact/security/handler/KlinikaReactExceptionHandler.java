package com.itrail.klinikreact.security.handler;

import java.util.NoSuchElementException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.itrail.klinikreact.response.BaseError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class KlinikaReactExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Flux<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Flux.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Flux<BaseError> errBaseResponse( NoSuchElementException ex ){
        return Flux.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Flux<BaseError> errBaseResponse( IllegalArgumentException ex ){
        return Flux.just( new BaseError( 404, ex.getMessage() ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<BaseError> errBaseResponse( BadCredentialsException ex ){
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }
    
}
