package com.itrail.klinikreact.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.models.Doctor;
import com.itrail.klinikreact.models.RehabilitationSolution;
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
@JsonInclude( Include.NON_NULL )
public class TreatmentResponse {

    private Long idTreatment;
    private LocalDateTime timeStartTreatment;
    private LocalDateTime endTimeTreatment;
    private DrugTreatmentResponse drug;
    private RehabilitationSolution rehabilitationSolution;
    private Long cardPatientId;
    private Doctor doctor;
    
}
