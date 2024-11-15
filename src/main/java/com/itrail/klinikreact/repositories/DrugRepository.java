package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Drug;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DrugRepository extends ReactiveCrudRepository<Drug, Long>  {

    @Query("SELECT d FROM drug d WHERE d.name = :name")
    Mono<Drug> findDrugByName( String name );
    
    @Query("SELECT d FROM drug d WHERE d.drugTreatment.idDrugTreatment = :idDrugTreatment")
    Flux<Drug> findDrugByIdDrugTreatment( Long idDrugTreatment );
}
