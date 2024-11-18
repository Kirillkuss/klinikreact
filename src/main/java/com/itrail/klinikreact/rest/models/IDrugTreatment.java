package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.Drug;
import com.itrail.klinikreact.models.DrugTreatment;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.DrugResponse;

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

@RequestMapping( value = "drug-treatments")
@Tag(name = "8. Drug Treatment", description = "Справочник: Медикаментозное лечение и препараты")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = DrugTreatment.class ))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class  ))) })
    })
public interface IDrugTreatment {

    @GetMapping(value = "/lazy/drug-treatment/{page}{size}")
    @Operation( description = "Список мед. лечений", summary = "Список мед. лечений")
    public Flux<DrugTreatment> getLazyDrugTreatment( @Parameter( description = "Страница", example = "1" ) int page,
                                                     @Parameter( description = "кол-во", example = "4" ) int size );

    @GetMapping( value ="/lazy/drug/{page}{size}")
    @Operation( description = "Список препаратов", summary = "Список препаратов")
    public Flux<Drug> getLayDrug( @Parameter( description = "Страница", example = "1" ) int page,
                                  @Parameter( description = "кол-во", example = "4" ) int size );

    @GetMapping( value = "/{id}")
    @Operation( description = "Список препаратов по Ид мед. лечения", summary = "Список препаратов по Ид мед. лечения")
    public Flux<DrugResponse> getListDrugByIdDrugTreatment( @Parameter( description = "Ид мед. лечения", example = "1" ) Long id );

    @PostMapping( value = "/add/drug-treatment")
    @Operation( description = "Добавить медикаментозного лечения", summary = "Добавить медикаментозного лечения")
    public Mono<DrugTreatment> addDrugTreatment( @RequestBody DrugTreatment drugTreatment );

    @PostMapping( value = "/add/drug")
    @Operation( description = "Добавить препарат для медикаментозного лечения", summary = "Добавить препарат для медикаментозного лечения")
    public Mono<Drug> addDrug( @RequestBody Drug drug );
 

}
