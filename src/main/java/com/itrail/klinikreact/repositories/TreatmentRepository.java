package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Treatment;
import reactor.core.publisher.Flux;

public interface TreatmentRepository extends ReactiveCrudRepository<Treatment, Long>{

    @Query("SELECT t FROM Treatment t WHERE t.cardPatientId = :id AND (( t.timeStartTreatment >= :from) AND ( t.timeStartTreatment <= :to))" )
    Flux<Treatment> findByParamIdCardAndDateStart( Long id, LocalDateTime from, LocalDateTime to);

    @Query("SELECT t FROM Treatment t WHERE t.cardPatientId = :idCardPatient AND t.rehabilitationSolution.idRehabilitationSolution = :idRehabilitationSolution" )
    Flux<Treatment> findByParamIdCardAndIdRh( Long idCardPatient, Long idRehabilitationSolution);

}
