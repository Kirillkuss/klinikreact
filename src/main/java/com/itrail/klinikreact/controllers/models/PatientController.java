package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.aspect.log.ExecuteTimeLog;
import com.itrail.klinikreact.repositories.PatientRepository;
import com.itrail.klinikreact.request.PatientRequest;
import com.itrail.klinikreact.response.PatientResponse;
import com.itrail.klinikreact.rest.models.IPatient;
import com.itrail.klinikreact.services.model.PatientService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PatientController implements IPatient {

    private final PatientService patientService;
    private final PatientRepository patientRepository;

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
