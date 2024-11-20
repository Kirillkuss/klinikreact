package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.RehabilitationSolution;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.rest.models.IRehabilitationSolution;
import com.itrail.klinikreact.services.model.RehabilitationSolutionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RehabilitationSolutionController implements IRehabilitationSolution{

    private final RehabilitationSolutionService rehabilitationSolutionService;

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
    public Flux<RehabilitationSolution> getLazyRehabilitationSolution(int page, int size) {
        return rehabilitationSolutionService.getLazyRehabilitationSolution( page, size );
    }

    @Override
    public Mono<RehabilitationSolution> getRehabilitationSolutionByName(String name) {
        return rehabilitationSolutionService.getRehabilitationSolutionByName( name );
    }

    @Override
    public Mono<RehabilitationSolution> addRehabilitationSolution(RehabilitationSolution rehabilitationSolution) {
        return rehabilitationSolutionService.addRehabilitationSolution( rehabilitationSolution );
    }
    
}
