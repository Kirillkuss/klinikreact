package com.itrail.klinikreact.response.report;

import java.util.List;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.response.CardPatientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class CardPatientReport {
    
    private CardPatientResponse cardPatient;
    private Integer countTreatment;
    private List<RehabilitationSolutionReport> treatment;
}
