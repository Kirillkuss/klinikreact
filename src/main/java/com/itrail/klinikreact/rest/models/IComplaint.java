package com.itrail.klinikreact.rest.models;

import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.Complaint;
import com.itrail.klinikreact.models.TypeComplaint;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.TypeComplaintResponse;
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

@RequestMapping( value = "complaints")
@Tag(name = "6. Сomplaint", description = "Справочник: Жалобы и под жалобы ")
@ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Успешно",        content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = Complaint.class))) }),
        @ApiResponse( responseCode = "400", description = "Плохой запрос",  content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) }),
        @ApiResponse( responseCode = "500", description = "Ошибка сервера", content = { @Content( mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema( implementation = BaseError.class))) })
    })
public interface IComplaint {

    @GetMapping(value = "/lazy-complaints/{page}{size}")
    @Operation( description = "Получение справочника жалобы", summary = "Получение справочника жалобы")
    public Flux<Complaint> getLazyComplaints( @Parameter( description = "Страница", example = "1" ) int page,
                                              @Parameter( description = "кол-во", example = "10" ) int size );

    @PostMapping( value = "/complaint/add")
    @Operation( description = "Добавление справочника жалобы", summary = "Добавление справочника жалобы")
    public Mono<Complaint> getAddComplaint( @RequestBody Complaint complaint );

    @GetMapping(value = "/lazy-type-complaints/{page}{size}")
    @Operation( description = "Получение справочника под жалобы", summary = "Получение справочника под жалобы")
    public Flux<TypeComplaint> getLazyTypeComplaints( @Parameter( description = "Страница", example = "1" ) int page,
                                                     @Parameter( description = "кол-во", example = "10" ) int size );

    @PostMapping( value = "/type-complaint/add")
    @Operation( description = "Добавление справочника под жалобы", summary = "Добавление под справочника жалобы")
    public Mono<TypeComplaint> getAddComplaint( @RequestBody TypeComplaint typeComplaint );

    @GetMapping(value = "/list-type-complaints/{id}")
    @Operation( description = "Получение справочника под жалобы", summary = "Получение справочника под жалобы")
    public Mono<TypeComplaintResponse> getTypeComlaintsByIdComplaint( @Parameter( description = "Ид жалобы", example = "1" ) Long id );

    
}
