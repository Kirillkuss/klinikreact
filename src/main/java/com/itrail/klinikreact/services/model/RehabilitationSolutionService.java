package com.itrail.klinikreact.services.model;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.aspect.log.ExecuteTimeLog;
import com.itrail.klinikreact.models.RehabilitationSolution;
import com.itrail.klinikreact.repositories.RehabilitationSolutionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RehabilitationSolutionService {
    
    private final RehabilitationSolutionRepository rehabilitationSolutionRepository;

    @ExecuteTimeLog(operation = "getLazyRehabilitationSolution")
    public Flux<RehabilitationSolution> getLazyRehabilitationSolution(int page, int size ){
        return rehabilitationSolutionRepository.findAll() 
                .skip((page - 1) * size)
                .take(size);
    }

    @ExecuteTimeLog(operation = "getRehabilitationSolutionByName")
    public Mono<RehabilitationSolution> getRehabilitationSolutionByName( String name ){
        return rehabilitationSolutionRepository.findRehabilitationSolutionByName( name )
            .switchIfEmpty(Mono.error( new NoSuchElementException("Ребилитационное лечение c таким наименованием не существует")));
    }

    @ExecuteTimeLog(operation = "addRehabilitationSolution")
    public Mono<RehabilitationSolution> addRehabilitationSolution( RehabilitationSolution rehabilitationSolution ){
        return rehabilitationSolutionRepository.findRehabilitationSolutionByName(rehabilitationSolution.getName())
            .flatMap( foundrehabilitationSolution -> Mono.error( new IllegalArgumentException( "Ребилитационное лечение с таким наименованием уже существует")))
                .then( rehabilitationSolutionRepository.save( rehabilitationSolution ));
    }


}
