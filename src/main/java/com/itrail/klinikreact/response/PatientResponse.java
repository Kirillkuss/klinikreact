package com.itrail.klinikreact.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.models.Document;
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
@JsonInclude( Include.NON_NULL )
public class PatientResponse extends Patient {
    
    private Document document;
}
