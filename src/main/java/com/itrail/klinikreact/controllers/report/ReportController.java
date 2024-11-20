package com.itrail.klinikreact.controllers.report;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.report.CardPatientReport;
import com.itrail.klinikreact.response.report.DrugTreatmentReport;
import com.itrail.klinikreact.response.report.RecordPatientReport;
import com.itrail.klinikreact.response.report.RehabilitationSolutionReport;
import com.itrail.klinikreact.rest.report.IReport;
import com.itrail.klinikreact.services.report.ReportService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReportController implements IReport {

    private final ReportService reportService;

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
    public Flux<RehabilitationSolutionReport> getStatRehabilitationSolution(LocalDateTime from, LocalDateTime to)  {
        return reportService.getStatRehabilitationSolution( from, to );
    }

    @Override
    public Flux<DrugTreatmentReport> getStatDrugTreatment(LocalDateTime from, LocalDateTime to) {
        return reportService.getStatDrugTreatment( from, to );
    }

    @Override
    public Mono<CardPatientReport> getStatPatientTreatment(Long idCardPatient) {
        return reportService.getStatPatientTreatment( idCardPatient );
    }

    @Override
    public Mono<RecordPatientReport> getStatRecordPatientByPatientAndTime(RecordPatientRequest recordPatientRequest){
        return reportService.getStatRecordPatientByPatientAndTime( recordPatientRequest );
    }
    
}
