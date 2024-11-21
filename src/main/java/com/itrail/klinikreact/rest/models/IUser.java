package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itrail.klinikreact.request.UserRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag( name = "USERS", description = "CRUD USERS" )
@RequestMapping( "users" )
@ApiResponses(value = {
    @ApiResponse( responseCode = "200", description = "Success",            content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = UserResponse.class ))) }),
    @ApiResponse( responseCode = "400", description = "Bad request",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
    @ApiResponse( responseCode = "500", description = "System malfunction", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) })
    })
public interface IUser {

    @RequestMapping( method = RequestMethod.GET )
    @Operation( description = "List users", summary = " List Users ")
    public Flux<UserResponse> getUsers();

    @RequestMapping(method = RequestMethod.POST)
    @Operation( description = "Добавить user", summary = "Добавить user")
    public Mono<UserResponse>  addUser( @RequestBody UserRequest userRequest);
    
}
