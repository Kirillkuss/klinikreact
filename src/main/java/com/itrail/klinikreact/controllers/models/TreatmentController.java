package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Treatment;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.rest.models.ITreatment;
import com.itrail.klinikreact.services.TreatmentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TreatmentController implements ITreatment {

    private final TreatmentService treatmentService;

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
    public Flux<Treatment> getLazyTreatment(int page, int size) {
        return treatmentService.getLazyTreatment( page, size );
    }

    @Override
    public Flux<Treatment> getTreatmentByParamIdCardAndDateStart( RecordPatientRequest recordPatientRequest ) {
        return treatmentService.findByParamIdCardAndDateStart(recordPatientRequest.getIdCardPatient(), recordPatientRequest.getFrom(), recordPatientRequest.getTo());
    }

    @Override
    public Flux<Treatment> getTreatmentByParamIdCardAndIdRh(Long idCardPatient, Long idRehabilitationSolution) {
        return treatmentService.findByParamIdCardAndIdRh( idCardPatient, idRehabilitationSolution );
    }

    @Override
    public Mono<Treatment> addTreatment(Treatment treatment) {
        return treatmentService.addTreatment( treatment );
    }
    
}
