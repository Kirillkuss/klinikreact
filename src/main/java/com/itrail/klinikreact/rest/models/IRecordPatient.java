package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.RecordPatient;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.RecordPatientResponse;
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

@RequestMapping( value = "record-patients")
@Tag(name = "5. Records Patients", description = "Записи пациентов:")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = RecordPatient.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) })
    })
public interface IRecordPatient {

    @GetMapping("/lazy/{page}{size}")
    @Operation( description = "Список записей паицента к врачу ", summary = "Список записей паицента к врачу")
    public Flux<RecordPatientResponse> getLazyRecordPatient( @Parameter( description = "Страница", example = "1" ) int page,
                                                             @Parameter( description = "кол-во", example = "10" ) int size );


    @PostMapping("/add")
    @Operation( description = "Добавить запись пациента к врачу", summary = "Добавить запись пациента к врачу")
    public Mono<RecordPatientResponse> addRecordPatient( @RequestBody RecordPatient recordPatient );


    @GetMapping("/find/{request}")
    @Operation( description = "Список всех записей пациентов к врачу по параметрам", summary = "Список всех записей пациентов к врачу по параметрам ")
    public Flux<RecordPatientResponse> getRecordPatientByParam( RecordPatientRequest request );
}
