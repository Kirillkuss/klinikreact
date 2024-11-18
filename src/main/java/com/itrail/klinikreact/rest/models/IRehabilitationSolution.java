package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.RehabilitationSolution;
import com.itrail.klinikreact.response.BaseError;
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

@RequestMapping( value = "rehabilitation-treatments")
@Tag(name = "9. Rehabilitation Treatment", description = "Справочник: Реабилитационное лечение")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = RehabilitationSolution.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) })
    })
public interface IRehabilitationSolution {

    @GetMapping("/lazy/{page}{size}")
    @Operation( description = "Получение списка всех лечений", summary = "Получение списка всех лечений")
    public Flux<RehabilitationSolution> getLazyRehabilitationSolution( @Parameter( description = "Страница", example = "1" ) int page,
                                                                       @Parameter( description = "кол-во", example = "10" ) int size );

    @GetMapping("/{name}")
    @Operation( description = "Поиск по названию лечения", summary = "Поиск по названию лечения")
    public Mono<RehabilitationSolution> getRehabilitationSolutionByName( @Parameter( description = "Наименование лечения") String name );
    
    @PostMapping("/add")
    @Operation( description = "Добавить способ лечения", summary = "Добавить способ лечения")
    public Mono<RehabilitationSolution> addRehabilitationSolution( @RequestBody RehabilitationSolution rehabilitationSolution );

}
