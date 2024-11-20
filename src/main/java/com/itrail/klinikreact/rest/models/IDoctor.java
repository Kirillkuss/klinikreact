package com.itrail.klinikreact.rest.models;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itrail.klinikreact.models.Doctor;
import com.itrail.klinikreact.response.BaseError;
import javax.ws.rs.core.MediaType;
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

@RequestMapping( value = "doctors")
@Tag( name = "1. Doctors", description = "Doctors")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Success",           content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema( schema = @Schema( implementation = Doctor.class ))) }),
        @ApiResponse( responseCode = "400", description = "Bad request",       content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema( schema = @Schema( implementation = BaseError.class ))) }),
        @ApiResponse( responseCode = "500", description = "System malfunction",content = { @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema( schema = @Schema( implementation = BaseError.class ))) })
    })
@SecurityRequirement(name = "Bearer Authentication")
public interface IDoctor {

    @GetMapping("/lazy/{page}{size}")
    @Operation( description ="Ленивая загурзка доктора", summary = "Ленивая загурзка доктора")
    public Flux<Doctor> getLazyDoctors( @Parameter( description = "Страница", example = "1" ) int page,
                                        @Parameter( description = "кол-во", example = "10" ) int size);

    @GetMapping("/{id}")
    @Operation( description ="Поиск по ид", summary = "Поиск по ид")
    public Mono<Doctor> getDoctorById(Long id);

    @GetMapping("/count")
    @Operation( description ="Кол-во докторов", summary = "Кол-во докторов")
    public Mono<Long> getCountDoctor();

    @PostMapping("/add")
    @Operation( description = "Добавить доктора", summary = "Добавить доктора")
    public Mono<Doctor> addDoctor( @RequestBody Doctor doctor );

    @GetMapping("/find-fio/{word}{page}{size}")
    @Operation( description = "Поиск врача по ФИО", summary = "Поиск врача по ФИО")
    Flux<Doctor> findDoctorsByFIO( @Parameter( description = "ФИО врача", example = "Попов") String word,
                                   @Parameter( description = "страница", example = "1")  int page,
                                   @Parameter( description = "размер", example = "10")    int size );
}
