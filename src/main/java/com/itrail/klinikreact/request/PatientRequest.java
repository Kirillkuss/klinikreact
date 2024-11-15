package com.itrail.klinikreact.request;

import com.itrail.klinikreact.models.Patient;
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
public class PatientRequest {
    
    private Patient patient;
    private Long idDocument;
}
