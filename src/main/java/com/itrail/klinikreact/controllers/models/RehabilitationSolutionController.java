package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.RehabilitationSolution;
import com.itrail.klinikreact.rest.models.IRehabilitationSolution;
import com.itrail.klinikreact.services.model.RehabilitationSolutionService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RehabilitationSolutionController implements IRehabilitationSolution{

    private final RehabilitationSolutionService rehabilitationSolutionService;

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
