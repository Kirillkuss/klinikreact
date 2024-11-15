package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.repositories.PatientRepository;
import com.itrail.klinikreact.request.PatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.PatientResponse;
import com.itrail.klinikreact.rest.models.IPatient;
import com.itrail.klinikreact.services.PatientService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PatientController implements IPatient {

    private final PatientService patientService;
    private final PatientRepository patientRepository;

    @ExceptionHandler(Throwable.class)
    public Flux<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Flux.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Flux<BaseError> errBaseResponse( NoSuchElementException ex ){
        return Flux.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Flux<BaseError> errBaseResponse( IllegalArgumentException ex ){
        return Flux.just( new BaseError( 404, ex.getMessage() ));
    }

    @Override
    public Mono<PatientResponse> addPatient( PatientRequest patientRequest) {
        return patientService.addPatient( patientRequest );
    }

    @Override
    public Flux<PatientResponse> findByWord(String word) {
        return patientService.findPatientByFioAndPhone( word );
    }

    @Override
    public Flux<PatientResponse> getLazyLoadPatient(int page, int size) {
        return patientService.getLazyLoadPatient( page, size );
    }

    @Override
    @ExecuteTimeLog(operation = "getCountPatient")
    public Mono<Long> getCountPatient() {
        return patientRepository.count();
    }
    
}
