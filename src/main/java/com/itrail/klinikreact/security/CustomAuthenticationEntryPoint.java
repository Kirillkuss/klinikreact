package com.itrail.klinikreact.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrail.klinikreact.response.BaseError;
import reactor.core.publisher.Mono;

public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody;
        try {
            responseBody = objectMapper.writeValueAsString( new BaseError( 401, authException.getMessage() ));
        } catch (JsonProcessingException e) {
            responseBody = "{\"error\": \"Failed to serialize error message\"}";
        }

        return exchange.getResponse().writeWith( Mono.just( exchange.getResponse().bufferFactory().wrap( responseBody.getBytes() )));
    }
   
}

