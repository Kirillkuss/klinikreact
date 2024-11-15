package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.CardPatient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardPatientRepository extends ReactiveCrudRepository<CardPatient, Long>{

    @Query("SELECT cp.* FROM Card_patient cp WHERE cp.patient.idPatient = :idPatient")
    Mono<CardPatient> findCardPatientByIdPatient( Long idPatient );

     @Query( "SELECT cp.* FROM Card_patient cp \n" +
             "left join patient p on p.id_patient = cp.patient_id \n" +
             "left join document d on d.id_document = p.document_id \n" + 
             "WHERE d.numar LIKE :param \n" +
             "OR d.snils LIKE :param \n" +
             "OR d.polis LIKE :param OR \n" + 
             "CONCAT( p.surname, ' ',p.name, ' ', p.full_name ) LIKE :param ")
    Flux<CardPatient> findCardPatientByDocOrFIO( String param );

    @Query( "SELECT * Card_patient cp \n " + 
            "LEFT JOIN Card_patient_complaint cpc ON cpc.card_patient_id = cp.id_card_patient \n" + 
            "LEFT JOIN type_complaint tp ON tp.id_type_complaint = cpc.type_complaint_id \n" + 
            "WHERE cp.id_card_patient = ?1 AND tp.id_type_complaint = ?2")
    Mono<CardPatient> findCardPatientByIdAndIdTypeComplaint(Long idCardPatient, Long idTaypeCompaint );
    
}
