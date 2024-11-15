package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itrail.klinikreact.models.Document;
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

@RequestMapping( value = "documents")
@Tag(name = "3. Documents", description = "Документ пациента")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = Document.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class ))) })
    })
public interface IDocument {

    @Operation( description = "Добавить документ", summary = "Добавить документ")
    @RequestMapping( method = RequestMethod.POST , value = "/add")
    public Mono<Document> addDocument(@RequestBody Document document );

    @Operation( description = "Найти документ", summary = "Найти документ")
    @RequestMapping( method = RequestMethod.GET , value = "/find/{word}")
    public Flux<Document> findByWord( @Parameter( description = "Параметр поиска") String word ) ;

    @GetMapping(value = "/lazy/{page}{size}")
    @Operation( description = "Список документов", summary = "Список документов")
    public Flux<Document> getLazyDocument( @Parameter( description = "Страница", example = "1" ) int page,
                                           @Parameter( description = "кол-во", example = "10" ) int size);
    
    
    @GetMapping(value = "/count")
    @Operation( description = "Количество документов", summary = "Количество документов")
    public Mono<Long> getCountDocument();
    
}
