package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Doctor;
import reactor.core.publisher.Flux;

public interface DoctorRepository extends ReactiveCrudRepository<Doctor, Long>{

    @Query("SELECT d.* FROM doctor d WHERE CONCAT ( d.surname, ' ', d.name, ' ', d.full_name ) LIKE :word ")
    Flux<Doctor> findDoctorsByFIO( @Param("word") String word );
    
}
