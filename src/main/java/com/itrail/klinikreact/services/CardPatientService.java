package com.itrail.klinikreact.services;

import org.springframework.stereotype.Service;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.repositories.TypeComplaintRepository;
import com.itrail.klinikreact.response.CardPatientResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CardPatientService {
    
    private final CardPatientRepository cardPatientRepository;
    private final TypeComplaintRepository typeComplaintRepository;
    private final PatientService patientService;

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

}
