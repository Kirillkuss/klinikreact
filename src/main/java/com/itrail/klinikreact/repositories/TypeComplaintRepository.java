package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.TypeComplaint;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeComplaintRepository extends ReactiveCrudRepository<TypeComplaint, Long>{

    @Query( "SELECT tc.* from type_complaint tc WHERE tc.name = :name")
    Mono<TypeComplaint> findTypeComplaintByName( String name);

    @Query( "SELECT tc.id_type_complaint, tc.name FROM  type_complaint tc WHERE tc.complaint_id = :idComplaint")
    Flux<TypeComplaint> findTypeComplaintByIdComplaint( Long idComplaint );

    @Query("select tc.name from type_complaint tc \n" +
            "left join card_patient_complaint cpc on cpc.type_complaint_id = tc.id_type_complaint \n" +
            "left join card_patient cp on cp.id_card_patient = cpc.card_patient_id \n" +
            "where cp.id_card_patient = :idCardPatient")
    Flux<TypeComplaint> findTypeComplaintByIdCardPatient( Long idCardPatient );
    
}
