package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.RecordPatient;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.RecordPatientResponse;
import com.itrail.klinikreact.rest.models.IRecordPatient;
import com.itrail.klinikreact.services.model.RecordPatientService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RecordPatientController implements IRecordPatient {

    private final RecordPatientService recordPatientService;

    @Override
    public Flux<RecordPatientResponse> getLazyRecordPatient(int page, int size) {
        return recordPatientService.getLazyRecordPatient( page, size );
    }

    @Override
    public Mono<RecordPatientResponse> addRecordPatient(RecordPatient recordPatient) {
        return recordPatientService.addRecordPatient( recordPatient );
    }

    @Override
    public Flux<RecordPatientResponse> getRecordPatientByParam(RecordPatientRequest recordPatientRequest) {
        System.out.println(recordPatientRequest );
        return recordPatientService.getRecordPatientByParam( recordPatientRequest.getId(),
                                                             recordPatientRequest.getFrom(),
                                                             recordPatientRequest.getTo());
    }
    
}
