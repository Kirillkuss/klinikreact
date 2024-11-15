package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.RecordPatient;
import reactor.core.publisher.Flux;

public interface RecordPatientRepository extends ReactiveCrudRepository<RecordPatient, Long>{   
    
    @Query("SELECT rp FROM RecordPatient rp WHERE rp.cardPatientId = :id AND (( rp.dateRecord >= :from) AND ( rp.dateRecord <= :to))" )
    Flux<RecordPatient> findByParamTwo( Long id, LocalDateTime from, LocalDateTime to);

    @Query( "SELECT rp.* FROM Record_patient rp \n " + 
            "LEFT JOIN Card_patient cp ON c.id_card_patient = card_patient_id \n" + 
            "LEFT JOIN Patient p ON p.id_patient = cp.patient_id \n" + 
            "WHERE p.id_patient = ?1 AND (rp.date_record BETWEEN ?2 and ?3 )"  )
    Flux<RecordPatient> findByParam( Long idCardPatient, LocalDateTime from, LocalDateTime to);
}
