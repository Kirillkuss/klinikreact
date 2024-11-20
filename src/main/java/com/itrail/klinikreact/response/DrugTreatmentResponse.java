package com.itrail.klinikreact.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.models.DrugTreatment;

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
public class DrugTreatmentResponse {

    private Long idDrug;
    private String name;
    private DrugTreatment drugTreatment;
    
}
