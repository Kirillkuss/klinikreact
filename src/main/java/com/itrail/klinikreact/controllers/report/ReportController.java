package com.itrail.klinikreact.controllers.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.aspect.security.SecuredControlFlux;
import com.itrail.klinikreact.aspect.security.SecuredControlMono;
import com.itrail.klinikreact.redis.model.UserSession;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import com.itrail.klinikreact.request.RecordPatientRequest;
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
    private final UserSessionRepository userSessionRepository;

    @Override
    @SecuredControlFlux(roles = {"ROLE_1", "ROLE_0"})
    public Flux<RehabilitationSolutionReport> getStatRehabilitationSolution(LocalDateTime from, LocalDateTime to)  {
        return reportService.getStatRehabilitationSolution( from, to );
    }

    @Override
    @SecuredControlFlux(roles = {"ROLE_1", "ROLE_0"})
    public Flux<DrugTreatmentReport> getStatDrugTreatment(LocalDateTime from, LocalDateTime to) {
        return reportService.getStatDrugTreatment( from, to );
    }

    @Override
    @SecuredControlMono(roles = {"ROLE_1", "ROLE_0"}) 
    public Mono<CardPatientReport> getStatPatientTreatment(Long idCardPatient) {
        return reportService.getStatPatientTreatment( idCardPatient );
    }

    @Override
    @SecuredControlMono(roles = {"ROLE_1", "ROLE_0"}) 
    public Mono<RecordPatientReport> getStatRecordPatientByPatientAndTime(RecordPatientRequest recordPatientRequest){
        return reportService.getStatRecordPatientByPatientAndTime( recordPatientRequest );
    }

    @Override
    public Flux<List<UserSession>> getUsers() {
        List<UserSession> response = new ArrayList<>();
        userSessionRepository.findAll().iterator().forEachRemaining( response::add );
        return Flux.just( response );
    } 
}
