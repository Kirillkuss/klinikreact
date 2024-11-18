package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itrail.klinikreact.models.Patient;
import com.itrail.klinikreact.request.PatientRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.PatientResponse;

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

@RequestMapping( value = "patients")
@Tag(name = "2. Patient", description = "Пациенты:")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = PatientResponse.class ))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) })
    })
public interface IPatient {
    
    @PostMapping( value = "/add")
    @Operation( description = "Добавить пациента", summary = "Добавить пациента")
    public Mono<PatientResponse> addPatient( @RequestBody PatientRequest patientRequest );
    
    @RequestMapping( method = RequestMethod.GET, value = "/find/{word}")
    @Operation( description = "Поиск пациента по ФИО или номеру телефона", summary = "Поиск пациента по ФИО или номеру телефона")
    public Flux<PatientResponse> findByWord( @Parameter( description = "Параметр поиска")  String word );

    @GetMapping(value = "/lazy/{page}{size}")
    @Operation( description = "ленивая загрузка пациентов", summary = "ленивая загрузка пациентов")
    public Flux<PatientResponse> getLazyLoadPatient( @Parameter( description = "Страница", example = "1" ) int page,
                                                     @Parameter( description = "кол-во", example = "10" ) int size) ;

    @GetMapping(value = "/count")
    @Operation( description ="Количество пациентов", summary = "Количество пациентов")
    public Mono<Long> getCountPatient( ) ;
}
