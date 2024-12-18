package com.itrail.klinikreact.services.model;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.aspect.log.ExecuteTimeLog;
import com.itrail.klinikreact.models.DrugTreatment;
import com.itrail.klinikreact.models.Treatment;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.repositories.DoctorRepository;
import com.itrail.klinikreact.repositories.DrugRepository;
import com.itrail.klinikreact.repositories.DrugTreatmentRepository;
import com.itrail.klinikreact.repositories.RehabilitationSolutionRepository;
import com.itrail.klinikreact.repositories.TreatmentRepository;
import com.itrail.klinikreact.response.DrugTreatmentResponse;
import com.itrail.klinikreact.response.TreatmentResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TreatmentService {
    
    private final TreatmentRepository              treatmentRepository;
    private final DrugRepository                   drugRepository;
    private final DrugTreatmentRepository          drugTreatmentRepository;
    private final RehabilitationSolutionRepository rehabilitationSolutionRepository;
    private final CardPatientRepository            cardPatientRepository;
    private final DoctorRepository                 doctorRerository;

    /**
     * Ленивая загрузка леченией
     * @param page - страница
     * @param size - размер
     * @return Flux TreatmentResponse
     */
    @ExecuteTimeLog( operation = "getLazyTreatment")
    public Flux<TreatmentResponse> getLazyTreatment( int page, int size ){
        return getFluxTreatmentResponse( treatmentRepository.findAll().skip(( page - 1) * size ).take( size ));                       
    }
    /**
     * Поиск лечений по параметрам за промежуток времени
     * @param id -Ид карты пациента
     * @param from - дата начала
     * @param to   - Дата окончания
     * @return Flux TreatmentResponse
     */
    @ExecuteTimeLog( operation = "findByParamIdCardAndDateStart")
    public Flux<TreatmentResponse> findTreatmentByParamIdCardAndDateStart( Long id, LocalDateTime from, LocalDateTime to ){
        return getFluxTreatmentResponse( treatmentRepository.findTreatmentByParamIdCardAndDateStart( id, from, to ))
            .switchIfEmpty( Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено!")));
    }
    /**
     * Получение списка лечений по ИД карте пациента и по типу реб. лечения
     * @param idCardPatient             - Ид карты пациента
     * @param idRehabilitationSolution  - Ид реб. леч.
     * @return Flux TreatmentResponse
     */
    @ExecuteTimeLog( operation = "findByParamIdCardAndDateStart")
    public Flux<TreatmentResponse> findTreatmentByParamIdCardAndIdRh( Long idCardPatient, Long idRehabilitationSolution){
        return getFluxTreatmentResponse( treatmentRepository.findTreatmentByParamIdCardAndIdRh( idCardPatient, idRehabilitationSolution ))
            .switchIfEmpty(Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено!")));
    }
    /**
     * Добавление лечения для пациента
     * @param treatment - входящий запрос
     * @return Mono TreatmentResponse
     */
    @ExecuteTimeLog( operation = "addTreatment")
    public Mono<TreatmentResponse> addTreatment( Treatment treatment ){
        return getMonoTreatmentResponse( checkAddTreatment( treatment ).then(treatmentRepository.save( treatment )));
    }
    /**
     * Проверка лечения
     * @param treatment - входящий запрос
     * @return Mono Void 
     */
    private Mono<Void> checkAddTreatment( Treatment treatment ){
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
                                return Mono.empty();
                            });
    }
    /**
     * Получение ответа для добавления лечения 
     * @param treatment - входящий запрос
     * @return Mono TreatmentResponse
     */
    private Mono<TreatmentResponse> getMonoTreatmentResponse( Mono<Treatment> treatment ){
        return treatment.flatMap( foundTreatment -> {
            return drugRepository.findById( foundTreatment.getDrugId() )
                .flatMap( drugFound ->{
                    return drugTreatmentRepository.findById( drugFound.getDrugTreatmentId() )
                        .flatMap( foundDrugTreatment ->{
                            return rehabilitationSolutionRepository.findById( foundTreatment.getRehabilitationSolutionId() )
                                .flatMap( foundRehabilitationSolutionRepository ->{
                                    return doctorRerository.findById( foundTreatment.getDoctorId() ).map( foundDoctor ->{
                                        foundTreatment.setDrugId(null );
                                        foundTreatment.setRehabilitationSolutionId(null );
                                        foundTreatment.setDoctorId(null );
                                        return new TreatmentResponse( foundTreatment.getIdTreatment(),
                                                                       foundTreatment.getTimeStartTreatment(),
                                                                       foundTreatment.getEndTimeTreatment(),
                                                                       new DrugTreatmentResponse( drugFound.getIdDrug(),
                                                                                                  drugFound.getName(),
                                                                                                  new DrugTreatment(null,
                                                                                                                    foundDrugTreatment.getName() )),
                                                                        foundRehabilitationSolutionRepository,
                                                                        foundTreatment.getCardPatientId(),
                                                                        foundDoctor );
                                    });
                                });
                        });
                });
        });     
    }

    /**
     * Получение ответа при поиске
     * @param treatment - входной параметр
     * @return Flux TreatmentResponse 
     */
    private Flux<TreatmentResponse> getFluxTreatmentResponse( Flux<Treatment> treatment ){
        return treatment.flatMap( foundTreatment -> {
            return drugRepository.findById( foundTreatment.getDrugId() )
                .flatMap( drugFound ->{
                    return drugTreatmentRepository.findById( drugFound.getDrugTreatmentId() )
                        .flatMap( foundDrugTreatment ->{
                            return rehabilitationSolutionRepository.findById( foundTreatment.getRehabilitationSolutionId() )
                                .flatMap( foundRehabilitationSolutionRepository ->{
                                    return doctorRerository.findById( foundTreatment.getDoctorId() ).map( foundDoctor ->{
                                        foundTreatment.setDrugId(null );
                                        foundTreatment.setRehabilitationSolutionId(null );
                                        foundTreatment.setDoctorId(null );
                                        return new TreatmentResponse( foundTreatment.getIdTreatment(),
                                                                       foundTreatment.getTimeStartTreatment(),
                                                                       foundTreatment.getEndTimeTreatment(),
                                                                       new DrugTreatmentResponse( drugFound.getIdDrug(),
                                                                                                  drugFound.getName(),
                                                                                                  new DrugTreatment(null,
                                                                                                                    foundDrugTreatment.getName() )),
                                                                        foundRehabilitationSolutionRepository,
                                                                        foundTreatment.getCardPatientId(),
                                                                        foundDoctor );
                                    });
                                });
                        });
                });
        });     
    }

}
