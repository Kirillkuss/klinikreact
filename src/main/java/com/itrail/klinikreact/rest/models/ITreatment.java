package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.Treatment;
import com.itrail.klinikreact.request.RecordPatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.TreatmentResponse;

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

@RequestMapping( value = "treatments")
@Tag(name = "7. Treatment", description = "Лечение пациентов:")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = TreatmentResponse.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) })
    })
public interface ITreatment {

    @GetMapping("/lazy/{page}{size}")
    @Operation( description = "Получение списка всех лечений", summary = "Получение списка всех лечений")
    public Flux<TreatmentResponse> getLazyTreatment( @Parameter( description = "Страница", example = "1" ) int page,
                                             @Parameter( description = "кол-во", example = "10" ) int size );

    @GetMapping("/find/{recordPatientRequest}")
    @Operation( description = "Получение списка лечений по параметрам", summary = "Получение списка лечений по параметрам")
    public Flux<TreatmentResponse> getTreatmentByParamIdCardAndDateStart( RecordPatientRequest recordPatientRequest ); 

    @GetMapping("/{idCardPatient}{idRehabilitationSolution}")
    @Operation( description = "Получение списка лечений по параметрам", summary = "Получение списка лечений по параметрам")
    public Flux<TreatmentResponse> getTreatmentByParamIdCardAndIdRh( @Parameter( description = "Ид карты пациента", example = "1" ) Long idCardPatient,
                                                             @Parameter( description = "Ид реабилитационного лечения", example = "1" ) Long idRehabilitationSolution );
    
    @PostMapping("/add")
    @Operation( description = "Добавить лечение для пациента", summary = "Добавить лечение для пациента")
    public Mono<TreatmentResponse> addTreatment( @RequestBody Treatment treatment );

    
}
