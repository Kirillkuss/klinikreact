package com.itrail.klinikreact.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.models.TypeComplaint;
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
public class CardPatientResponse {
    
    private CardPatient cardPatient;
    private List<TypeComplaint> typeComplaints; 
    private PatientResponse patient;
}
