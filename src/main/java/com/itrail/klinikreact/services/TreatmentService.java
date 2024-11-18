package com.itrail.klinikreact.services;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.Treatment;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.repositories.DoctorRepository;
import com.itrail.klinikreact.repositories.DrugRepository;
import com.itrail.klinikreact.repositories.RehabilitationSolutionRepository;
import com.itrail.klinikreact.repositories.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TreatmentService {
    
    private final TreatmentRepository              treatmentRepository;
    private final DrugRepository                   drugRepository;
    private final RehabilitationSolutionRepository rehabilitationSolutionRepository;
    private final CardPatientRepository            cardPatientRepository;
    private final DoctorRepository                 doctorRerository;

    public Flux<Treatment> getLazyTreatment( int page, int size ){
        return treatmentRepository.findAll().skip(( page - 1) *size ).take( size );
    }

    public Flux<Treatment> findByParamIdCardAndDateStart( Long id, LocalDateTime from, LocalDateTime to ){
        return treatmentRepository.findByParamIdCardAndDateStart( id, from, to )
            .switchIfEmpty(Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено!")));
    }

    public Flux<Treatment> findByParamIdCardAndIdRh( Long idCardPatient, Long idRehabilitationSolution){
        return treatmentRepository.findByParamIdCardAndIdRh( idCardPatient, idRehabilitationSolution )
            .switchIfEmpty(Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено!")));
    }

    public Mono<Treatment> addTreatment( Treatment treatment ){
        return drugRepository.findById( treatment.getDrugId() )
                .switchIfEmpty( Mono.error( new IllegalArgumentException( "Указано неверное значение препарата, укажите другой !")))
                .then( rehabilitationSolutionRepository.findById( treatment.getRehabilitationSolutionId() ))
                    .switchIfEmpty(Mono.error( new IllegalArgumentException("Указано неверное значение реабилитационного лечения, укажите другой !")))
                    .then( cardPatientRepository.findById( treatment.getCardPatientId() )
                        .switchIfEmpty( Mono.error( new IllegalArgumentException( "Указано неверное значение карты пациента, укажите другой !"))))
                        .then( doctorRerository.findById( treatment.getDoctorId() )
                            .switchIfEmpty( Mono.error( new IllegalArgumentException( "Указано неверное значение ид доктора, укажите другой !"))))
                            .flatMap( t -> {
                                if( treatment.getEndTimeTreatment().isBefore( treatment.getTimeStartTreatment() )){
                                    return Mono.error( new IllegalArgumentException( "Дата окончания не может быть раньше даты начала лечения!") );
                                }
                                return treatmentRepository.save( treatment );
                            });

    }

}
