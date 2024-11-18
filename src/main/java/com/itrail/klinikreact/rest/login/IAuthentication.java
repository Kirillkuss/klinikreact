package com.itrail.klinikreact.rest.login;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@ApiResponses(value = {
        @ApiResponse( responseCode = "200" , description = "Authentication success", content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(  ))) }),
        @ApiResponse( responseCode = "400", description = "Bad Request",             content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(  ))) }),
        @ApiResponse( responseCode = "500", description = "System malfunction",      content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( ))) })
})
public interface IAuthentication {

    @GetMapping(value = "login")
    public Mono<String> login();

    @GetMapping(value = "index")
    public Mono<String> index();

    @GetMapping(value = "change-password")
    public Mono<String> changePassword();

   /**  @PostMapping(value = "change-password")
    public String requestPasswordChange( @RequestParam("user") String user, HttpServletRequest request, RedirectAttributes redirectAttributes );
    
    @PostMapping(value = "clear-error-message", produces = MediaType.APPLICATION_JSON)
    public String clearErrorMessage(HttpServletRequest request);*/
}