package com.itrail.klinikreact.services.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import com.itrail.klinikreact.repositories.RecordPatientRepository;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.report.CardPatientReport;
import com.itrail.klinikreact.response.report.DrugTreatmentReport;
import com.itrail.klinikreact.response.report.RecordPatientReport;
import com.itrail.klinikreact.response.report.RehabilitationSolutionReport;
import com.itrail.klinikreact.services.model.CardPatientService;
import com.itrail.klinikreact.services.model.PatientService;
import com.itrail.klinikreact.services.model.RecordPatientService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final DatabaseClient databaseClient;
    private final CardPatientService cardPatientService;
    private final RecordPatientService recordPatientService;

    public Flux<RehabilitationSolutionReport> getStatRehabilitationSolution(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return databaseClient.sql("SELECT * FROM public.report_stat_two(:dateFrom, :dateTo)")
            .bind("dateFrom", dateFrom)
            .bind("dateTo", dateTo)
            .map((row, rowMetadata) -> new RehabilitationSolutionReport(
                row.get("name_solution", String.class),
                row.get("count_solution", Long.class),
                row.get("count_patient", Long.class)
            ))
            .all();
    }

    public Flux<DrugTreatmentReport> getStatDrugTreatment( LocalDateTime dateFrom, LocalDateTime dateTo ){
        return databaseClient.sql("SELECT * FROM public.report_stat_drug_two( :dateFrom, :dateTo)")
            .bind( "dateFrom", dateFrom )
            .bind( "dateTo", dateTo )
            .map((row, rowMetaData) -> new DrugTreatmentReport( 
                row.get( "name",String.class),
                row.get( "count_drug_treatment", Long.class),
                row.get("count_patient", Long.class)
                ))
            .all();
    }

    public Mono<CardPatientReport> getStatPatientTreatment( Long inCardPatient ){
        CardPatientReport cardPatientReport = new CardPatientReport();
        return cardPatientService.findCardPatientById(inCardPatient)
            .flatMap( cardPatient -> {
                return getRehabilitationTreatment( inCardPatient )
                    .collectList()
                    .map( list -> {
                        cardPatientReport.setCardPatient( cardPatient );
                        cardPatientReport.setTreatment( list );
                        return cardPatientReport;
                    });
            });
    }

    private Flux<RehabilitationSolutionReport> getRehabilitationTreatment( Long inCardPatient ){
        return databaseClient.sql( "SELECT * FROM public.record_patient_two( :idCardPatient)")
            .bind("idCardPatient", inCardPatient)
            .map((row, rowMetaData) -> {
                RehabilitationSolutionReport rehabilitationSolutionReport = new RehabilitationSolutionReport();
                                             rehabilitationSolutionReport.setCountTreatment(row.get("count_solution", Long.class));
                                             rehabilitationSolutionReport.setNameRehabilitationTreatment(row.get("name_solution", String.class));
                return rehabilitationSolutionReport;
            }).all();    
    }

    public Mono<RecordPatientReport> getStatRecordPatientByPatientAndTime( RecordPatientRequest recordPatientRequest ){
        return cardPatientService.findCardPatientById( recordPatientRequest.getId() ).flatMap( cardPatient ->{
            return recordPatientService.getRecordPatientByParamAndIdPatient(recordPatientRequest.getId(), recordPatientRequest.getFrom(), recordPatientRequest.getTo()).collectList().map( list -> {
                return new RecordPatientReport( cardPatient, list.stream().count(), list );
            });
        });

    }



}
