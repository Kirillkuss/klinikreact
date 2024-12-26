package com.itrail.klinikreact.security.handler;

import java.net.URI;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class KlinikaReactErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public KlinikaReactErrorWebExceptionHandler( KlinikaReactErrorAttributes klinikaReactErrorAttributes,
                                                 ApplicationContext applicationContext,
                                                 ServerCodecConfigurer serverCodecConfigurer) {
        super( klinikaReactErrorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters( serverCodecConfigurer.getWriters());
        super.setMessageReaders( serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        return ServerResponse.status(HttpStatus.FOUND) 
                .location(URI.create("/react/error"))
                .build();
    }
        
}