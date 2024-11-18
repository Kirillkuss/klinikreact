package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.DrugTreatment;
import reactor.core.publisher.Mono;

public interface DrugTreatmentRepository extends ReactiveCrudRepository<DrugTreatment, Long>{

    @Query( "SELECT dt.* FROM drug_treatment dt WHERE dt.name = :name")
    Mono<DrugTreatment> findDrugTreatmentByName( String name );
    
}
