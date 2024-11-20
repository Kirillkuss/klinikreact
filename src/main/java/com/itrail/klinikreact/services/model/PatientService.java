package com.itrail.klinikreact.services.model;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.models.Patient;
import com.itrail.klinikreact.repositories.DocumentRepository;
import com.itrail.klinikreact.repositories.PatientRepository;
import com.itrail.klinikreact.request.PatientRequest;
import com.itrail.klinikreact.response.PatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final DocumentRepository documentRepository;

    @ExecuteTimeLog(operation = "addPatient")
    public Mono<PatientResponse> addPatient(PatientRequest patientRequest) {
        PatientResponse patientResponse = new PatientResponse();
        checkGender(patientRequest);
        return checkPatient(patientRequest)
            .then(documentRepository.findById(patientRequest.getIdDocument()))
            .flatMap(patient -> {
                patientRequest.getPatient().setDocumentId(patientRequest.getIdDocument());
                return patientRepository.save(patientRequest.getPatient())
                    .flatMap(savedPatient -> {
                        return documentRepository.findById(patientRequest.getIdDocument())
                            .flatMap(document -> {
                                patientResponse.setIdPatient(savedPatient.getIdPatient());
                                patientResponse.setSurname(savedPatient.getSurname());
                                patientResponse.setName(savedPatient.getName());
                                patientResponse.setFullName(savedPatient.getFullName());
                                patientResponse.setGender(savedPatient.getGender());
                                patientResponse.setPhone(savedPatient.getPhone());
                                patientResponse.setAddress(savedPatient.getAddress());
                                patientResponse.setDocument(document);
                                return Mono.just(patientResponse);
                            });
                    });
            });
    }

    private void checkGender(PatientRequest patientRequest) {
        String gender = patientRequest.getPatient().getGender(); 
        if (!"0".equals( gender ) && !"1".equals( gender )) {
            throw new IllegalArgumentException("Пол должен быть '0' или '1'");
        }
    }

    private Mono<Void> checkPatient(PatientRequest patientRequest) {
        return documentRepository.findById(patientRequest.getIdDocument())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Документ с таким ИД не существует")))
                .then(patientRepository.findPatientByPhone(patientRequest.getPatient().getPhone())
                        .flatMap(foundPatient -> Mono.error(new IllegalArgumentException("Пользователь с таким номером телефона уже существует, укажите другой")))
                        .then(patientRepository.findPatientByIdDocument(patientRequest.getIdDocument())
                                .flatMap(foundPatient -> Mono.error(new IllegalArgumentException("Неверное значение ИД документа, попробуйте другой")))));
    }
    /**
     * Поиск по фио и тел
     * @param word - параметр для поиска
     * @return Flux Patient
     */
    @ExecuteTimeLog(operation = "findPatientByFioAndPhone")
    public Flux<PatientResponse> findPatientByFioAndPhone( String word ){
        return getFluxPatientResponse( patientRepository.findPatientByFioAndPhone( "%" + word + "%" ))
            .switchIfEmpty( Flux.error( new NoSuchElementException( "По данному запросу ничего не найдено")) );                 
    }
    /**
     * Ленивания загрузка
     * @param page - страница
     * @param size - размер
     * @return Flux Patient
     */
    @ExecuteTimeLog(operation = "getLazyLoadPatient")
    public Flux<PatientResponse> getLazyLoadPatient( int page, int size ) {
        return getFluxPatientResponse( patientRepository.findAll().skip((page - 1) * size).take(size));
    }

    private Flux<PatientResponse> getFluxPatientResponse( Flux<Patient> fluxPatient ){
        return fluxPatient.flatMap(patient -> {
            return documentRepository.findById(patient.getDocumentId())  
                    .map(document -> {
                        PatientResponse patientResponse = new PatientResponse();
                                        patientResponse.setIdPatient( patient.getIdPatient() );
                                        patientResponse.setSurname( patient.getSurname() );
                                        patientResponse.setName( patient.getName() );
                                        patientResponse.setFullName( patient.getFullName() );
                                        patientResponse.setGender( patient.getGender() );
                                        patientResponse.setPhone( patient.getPhone() );
                                        patientResponse.setAddress( patient.getAddress() );
                                        patientResponse.setDocument( document );
                        return patientResponse;
                    });
        });
    }
    
    public Mono<PatientResponse> getFindPatientById( Long idPatient ){
        return patientRepository.findById( idPatient ).flatMap( patient -> {
            return documentRepository.findById( patient.getDocumentId() ).map( document ->{
                PatientResponse patientResponse = new PatientResponse();
                                patientResponse.setIdPatient( patient.getIdPatient() );
                                patientResponse.setSurname( patient.getSurname() );
                                patientResponse.setName( patient.getName() );
                                patientResponse.setFullName( patient.getFullName() );
                                patientResponse.setGender( patient.getGender() );
                                patientResponse.setPhone( patient.getPhone() );
                                patientResponse.setAddress( patient.getAddress() );
                                patientResponse.setDocument( document );
                return patientResponse;
            });
        });
    }
 
}
