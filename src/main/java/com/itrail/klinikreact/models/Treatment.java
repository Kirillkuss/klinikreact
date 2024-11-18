package com.itrail.klinikreact.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table( name = "treatment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(Include.NON_NULL)
public class Treatment implements Serializable {
    
    @Id
    @Hidden
    @Column( name = "id_treatment")
    @Schema( name        = "idTreatment",
             description = "ИД лечения",
             example     = "100",
             required    = true )
    private Long idTreatment;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column( name = "time_start_treatment")
    @Schema( name        = "timeStartTreatment",
             description = "Дата начала лечения",
             example     = "2023-01-22 18:00:00.745",
             required    = true )
    private LocalDateTime timeStartTreatment;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column( name = "end_time_treatment")
    @Schema( name        = "endTimeTreatment",
             description = "Дата окончания лечения",
             example     = "2023-07-22 18:00:00.745",
             required    = true )
    private LocalDateTime endTimeTreatment;

    @Column( name = "drug_id")
    @Schema( name        = "drugId",
             example     = "1",
             required    = true)
    private Long drugId;

    @Column( name = "card_patient_id")
    @Schema( name        = "cardPatientId",
             description = "ИД карты пациента",
             example     = "1",
             required    = true )
    private Long cardPatientId;

    @Column( name = "doctor_id")
    @Schema( name        = "doctorId",
             description = "ИД доктора",
             example     = "1",
             required    = true )
    private Long doctorId;

    @Column( name = "rehabilitation_solution_id")
    @Schema( name        = "rehabilitationSolutionId",
             description = "ИД реб. леч.",
             example     = "1",
             required    = true )
    private Long rehabilitationSolutionId;




}
