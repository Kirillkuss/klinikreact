package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.request.TypeComplaintRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.CardPatientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping( value = "card-patients")
@Tag(name = "4. Card Patient", description = "Карта пациента")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = CardPatient.class ))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос ", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation =  BaseError.class ))) })
    })
    @SecurityRequirement(name = "Bearer Authentication")
public interface ICardPatient {
    
    @GetMapping(value = "/count")
    @Operation( description ="Количество карт", summary = "Количество карт")
    public Mono<Long> getCountCardPatient( ) ;

    @GetMapping(value = "/lazy/{page}{size}")
    @Operation( description = "Ленивая загрузка", summary = "Ленивая загрузка")
    public Flux<CardPatientResponse> getLazyCardPatient( @Parameter( description = "Страница", example ="1") int page,
                                                         @Parameter( description = "Размер", example ="10") int size );

    @GetMapping(value = "/{param}")
    @Operation( description = "Поиск карты пациента по документу пациента (СНИЛС, номер документа, ПОЛИС) или ФИО", summary = "Поиск карты пациента по документу пациента")
    public Flux<CardPatientResponse> getFindCardPatientByDocOrFIO( @Parameter( description = "ФИО или Документ пациента", example ="248469703") String param ); 

    @PostMapping( value = "/add")
    @Operation( description = "Добавить карту пациента", summary = "Добавить карту пациента")
    public Mono<CardPatientResponse> addCardPatient( @RequestBody CardPatient cardPatient );

    @PostMapping(value = "/type-complaint")
    @Operation( description = "Добавить под жалобу в карту пациента", summary = "Добавить под жалобу в карту пациента")
    public Mono<Void> addTypeComplaintToCardPatient(@RequestBody TypeComplaintRequest typeComplaintRequest);
    
}