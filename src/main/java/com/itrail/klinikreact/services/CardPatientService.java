package com.itrail.klinikreact.services;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.repositories.TypeComplaintRepository;
import com.itrail.klinikreact.request.TypeComplaintRequest;
import com.itrail.klinikreact.response.CardPatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardPatientService {
    
    private final CardPatientRepository cardPatientRepository;
    private final TypeComplaintRepository typeComplaintRepository;
    private final PatientService patientService;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @ExecuteTimeLog( operation = "getLazyCardPatient")
    public Flux<CardPatientResponse> getLazyCardPatient(int page, int size) {
        return cardPatientRepository.findAll()
                .skip((page - 1) * size)
                .take(size)
                .flatMap(cardPatient -> 
                    patientService.getFindPatientById(cardPatient.getPatientId())
                        .flatMap(patient -> 
                            typeComplaintRepository.findTypeComplaintByIdCardPatient(cardPatient.getIdCardPatient())
                                .collectList()
                                .map(typeComplaints -> { 
                                    cardPatient.setPatientId( null ); 
                                    CardPatientResponse cardPatientResponse = new CardPatientResponse();
                                    cardPatientResponse.setCardPatient(cardPatient);
                                    cardPatientResponse.setPatient(patient);
                                    cardPatientResponse.setTypeComplaints(typeComplaints); 
                                    return cardPatientResponse; 
                                })  
                        )
                );
    }

  /**
     * Поиск карты паицента по ФИО или по документу
     * @param param
     * @return Flux CardPatientResponse
     */
    @ExecuteTimeLog( operation = "findCardPatientByDocOrFIO")
    public Flux <CardPatientResponse> findCardPatientByDocOrFIO( String param ){
        return cardPatientRepository.findCardPatientByDocOrFIO( "%" + param + "%" )
            .flatMap(cardPatient -> 
            patientService.getFindPatientById(cardPatient.getPatientId())
                .flatMap(patient -> 
                    typeComplaintRepository.findTypeComplaintByIdCardPatient(cardPatient.getIdCardPatient())
                        .collectList()
                        .map(typeComplaints -> {
                            cardPatient.setPatientId( null ); 
                            CardPatientResponse cardPatientResponse = new CardPatientResponse();
                            cardPatientResponse.setCardPatient(cardPatient);
                            cardPatientResponse.setPatient(patient);
                            cardPatientResponse.setTypeComplaints(typeComplaints); 
                            return cardPatientResponse; 
                         })
                    )
            );
    }

    public Mono<CardPatientResponse> addCardPatient( CardPatient cardPatient){
        return cardPatientRepository.findCardPatientByIdPatient( cardPatient.getPatientId() )
                .flatMap( foundCardPatient -> Mono.error( new IllegalArgumentException( "Карта пациента с таким ИД пациента уже существует")))
                    .then( patientService.getFindPatientById( cardPatient.getPatientId())
                    .switchIfEmpty( Mono.error( new IllegalArgumentException( "Пациента с таким ИД не существует"))))
                        .then(cardPatientRepository.save( cardPatient )).flatMap( saveCardPatint -> 
                            patientService.getFindPatientById(saveCardPatint.getPatientId()).map( patient ->{
                                cardPatient.setPatientId( null );
                                CardPatientResponse cardPatientResponse = new CardPatientResponse();
                                                    cardPatientResponse.setCardPatient( cardPatient );
                                                    cardPatientResponse.setPatient( patient );
                                return cardPatientResponse;
                            })
                        );
    }

    @ExecuteTimeLog(operation = "addTypeComplaintToCardPatient")
    public Mono<Void> addTypeComplaintToCardPatient( TypeComplaintRequest typeComplaintRequest) {
        return check(typeComplaintRequest.getIdCardPatient(), typeComplaintRequest.getIdTypeComplaint())
            .then(addTypeComplaintToCardPatientSecond(typeComplaintRequest.getIdCardPatient(), typeComplaintRequest.getIdTypeComplaint() ));
    }
    
    private Mono<Void> check(Long idCardPatient, Long idTypeComplaint) {
        return cardPatientRepository.findById(idCardPatient)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Карта с таким ИД не существует")))
            .then(typeComplaintRepository.findById(idTypeComplaint)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Под жалобы с таким ИД не существует")))
                .then(cardPatientRepository.findCardPatientByIdAndIdTypeComplaint(idCardPatient, idTypeComplaint)
                    .flatMap(foundCardPatient -> 
                        Mono.error(new IllegalArgumentException("Под жалоба с таким ИД уже добавлена в карту пациента"))
                    )
                )
            );
    }
    
    private Mono<Void> addTypeComplaintToCardPatientSecond( Long idCardPatient, Long idTypeComplaint ){
        return r2dbcEntityTemplate.getDatabaseClient()
               .sql("INSERT INTO Card_patient_Complaint(card_patient_id, type_complaint_id) VALUES ($1, $2)")
               .bind("$1", idCardPatient)
               .bind("$2", idTypeComplaint)
               .then()//S.onErrorResume(e -> {return Mono.error( e );});
               .onErrorResume(e -> {
                   if (e instanceof Throwable) {
                       return Mono.empty(); 
                   }
                   return Mono.error(e); 
               });
       }

}
