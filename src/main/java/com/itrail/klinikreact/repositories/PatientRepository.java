package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Patient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PatientRepository extends ReactiveCrudRepository<Patient, Long>{

    @Query("SELECT p.* FROM patient p WHERE CONCAT (p.surname, ' ', p.name, ' ', p.full_name ) LIKE :param OR p.phone LIKE :param")
    Flux<Patient> findPatientByFioAndPhone( String param );
    
    @Query("SELECT p.* FROM patient p where p.document_id = :idDocument")
    Mono<Patient> findPatientByIdDocument( Long idDocument );

    @Query("SELECT p.* FROM patient p WHERE p.phone = :phone")
    Mono<Patient> findPatientByPhone( String phone );

}
