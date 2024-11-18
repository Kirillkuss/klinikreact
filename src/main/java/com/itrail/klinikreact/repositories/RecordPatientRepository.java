package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.RecordPatient;
import reactor.core.publisher.Flux;

public interface RecordPatientRepository extends ReactiveCrudRepository<RecordPatient, Long>{   
    
    @Query("SELECT rp.* FROM record_patient rp WHERE rp.card_patient_id = :id AND (( rp.date_record >= :from) AND ( rp.date_record <= :to))" )
    Flux<RecordPatient> findByParamTwo( Long id, LocalDateTime from, LocalDateTime to);

    @Query( "SELECT rp.* FROM Record_patient rp \n " + 
            "LEFT JOIN Card_patient cp ON cp.id_card_patient  = card_patient_id \n"  + 
            "WHERE cp.id_card_patient = :idCardPatient AND (rp.date_record BETWEEN :from and :to )"  )
    Flux<RecordPatient> findByParam( Long idCardPatient, LocalDateTime from, LocalDateTime to);
}
