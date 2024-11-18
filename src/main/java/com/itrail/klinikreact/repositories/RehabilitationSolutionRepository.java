package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.RehabilitationSolution;
import reactor.core.publisher.Mono;

public interface RehabilitationSolutionRepository extends ReactiveCrudRepository<RehabilitationSolution, Long>{ 

    @Query( "SELECT rs.* FROM rehabilitation_solution rs WHERE rs.name = :name")
    Mono<RehabilitationSolution> findRehabilitationSolutionByName( String name );
}
