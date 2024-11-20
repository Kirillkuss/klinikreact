package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Treatment;
import reactor.core.publisher.Flux;

public interface TreatmentRepository extends ReactiveCrudRepository<Treatment, Long>{

    @Query("SELECT t.* FROM Treatment t WHERE t.card_patient_id = :id AND (( t.time_start_treatment >= :from) AND ( t.time_start_treatment <= :to))" )
    Flux<Treatment> findTreatmentByParamIdCardAndDateStart( Long id, LocalDateTime from, LocalDateTime to);

    @Query("SELECT t.* FROM Treatment t WHERE t.card_patient_id = :idCardPatient AND t.rehabilitation_solution_id = :idRehabilitationSolution" )
    Flux<Treatment> findTreatmentByParamIdCardAndIdRh( Long idCardPatient, Long idRehabilitationSolution);

}
