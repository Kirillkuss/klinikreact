package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.itrail.klinikreact.models.User;
import com.itrail.klinikreact.request.UserRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.UserResponse;
import com.itrail.klinikreact.rest.models.IUser;
//import com.itrail.klinikreact.services.model.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements IUser{

    //private final UserService userService;

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

    @Override
    public Flux<UserResponse> getUsers() {
        //return userService.getUsers();
        return null;
    }

    @Override
    public Mono<UserResponse> addUser( UserRequest userRequest ) {
        //return userService.addUserUserRequest( userRequest );
        return null;
    }
    
}
