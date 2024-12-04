package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Treatment;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.TreatmentResponse;
import com.itrail.klinikreact.rest.models.ITreatment;
import com.itrail.klinikreact.services.model.TreatmentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TreatmentController implements ITreatment {

    private final TreatmentService treatmentService;

    @Override
    public Flux<TreatmentResponse> getLazyTreatment(int page, int size) {
        return treatmentService.getLazyTreatment( page, size );
    }

    @Override
    public Flux<TreatmentResponse> getTreatmentByParamIdCardAndDateStart( RecordPatientRequest recordPatientRequest ) {
        return treatmentService.findTreatmentByParamIdCardAndDateStart(recordPatientRequest.getId(), recordPatientRequest.getFrom(), recordPatientRequest.getTo());
    }

    @Override
    public Flux<TreatmentResponse> getTreatmentByParamIdCardAndIdRh(Long idCardPatient, Long idRehabilitationSolution) {
        return treatmentService.findTreatmentByParamIdCardAndIdRh( idCardPatient, idRehabilitationSolution );
    }

    @Override
    public Mono<TreatmentResponse> addTreatment(Treatment treatment) {
        return treatmentService.addTreatment( treatment );
    }
    
}
