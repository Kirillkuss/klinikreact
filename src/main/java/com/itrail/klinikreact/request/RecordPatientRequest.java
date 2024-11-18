package com.itrail.klinikreact.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecordPatientRequest {
    
    @Schema( name        = "idCardPatient",
             description = "ид карты пациента",
             example     = "1",
             required    = true )
    private Long idCardPatient;
    @Schema( name        = "from",
             description = "дата и время с какого",
             example     = "2021-01-24T14:02:35.584",
             required    = true )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    @Schema( name        = "to",
             description = "дата и время с какого",
             example     = "2021-01-24T14:02:35.584",
             required    = true )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;
}
