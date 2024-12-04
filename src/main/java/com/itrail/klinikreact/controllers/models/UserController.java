package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.request.UserRequest;
import com.itrail.klinikreact.response.UserResponse;
import com.itrail.klinikreact.rest.models.IUser;
import com.itrail.klinikreact.services.model.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements IUser{

    private final UserService userService;

    @Override
    public Flux<UserResponse> getUsers() {
        return userService.getUsers();
    }

    @Override
    public Mono<UserResponse> addUser( UserRequest userRequest ) {
        return userService.addUserUserRequest( userRequest );
    }
    
}
