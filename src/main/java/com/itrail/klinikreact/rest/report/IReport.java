package com.itrail.klinikreact.rest.report;

import java.time.LocalDateTime;
import javax.ws.rs.core.MediaType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.report.CardPatientReport;
import com.itrail.klinikreact.response.report.DrugTreatmentReport;
import com.itrail.klinikreact.response.report.RecordPatientReport;
import com.itrail.klinikreact.response.report.RehabilitationSolutionReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/reports")
@Tag(name = "Report", description = "Отчеты:")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = RehabilitationSolutionReport.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) })
    })
public interface IReport {

    @Operation( description = "Отчет по виду ребилитационного лечения за период времени", summary = "Отчет по виду ребилитационного лечения за период времени")
    @GetMapping("/rehabilitation-treatments/{from}{to}")
    public Flux<RehabilitationSolutionReport> getStatRehabilitationSolution( @Parameter( description = "Дата начала выборки:", example = "2021-05-24T14:02:35.584") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                             @Parameter( description = "Дата конца выборки:", example = "2023-12-24T14:02:35.584") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to );


    @Operation( description = "Отчет о медикаментозном лечении за период времени", summary = "Отчет о медикаментозном лечении за период времени")
    @GetMapping( "/drug-treatment/{from}{to}")
    public Flux<DrugTreatmentReport> getStatDrugTreatment(@Parameter( description = "Дата начала выборки:", example = "2021-05-24T14:02:35.584") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                         @Parameter( description = "Дата конца выборки:", example = "2023-12-24T14:02:35.584") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to );
    
    @GetMapping("/info-patient/{id-card}")
    @Operation( description = "Отчет о полной информации по пациенту", summary = "Отчет о полной информации по пациенту")
    public Mono<CardPatientReport> getStatPatientTreatment( @Parameter( description = "Ид карты пациента:", example = "1")  Long idCardPatient ) ;

    @Operation( description = "Отчет по записям пациента к врачу за период времени", summary = "Отчет по записям пациента к врачу за период времени")
    @GetMapping("/report-patient/{recordPatientRequest}")
    public Mono<RecordPatientReport> getStatRecordPatientByPatientAndTime( RecordPatientRequest recordPatientRequest ) throws Exception;
    
}
