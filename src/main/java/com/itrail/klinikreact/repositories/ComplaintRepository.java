package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Complaint;
import reactor.core.publisher.Mono;

public interface ComplaintRepository extends ReactiveCrudRepository<Complaint, Long> {

    @Query("SELECT c.* FROM Complaint c WHERE c.functional_impairment = :name")
    Mono<Complaint> findComplaintByName( String name );
}
