package com.itrail.klinikreact.response.report;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.response.CardPatientResponse;
import com.itrail.klinikreact.response.RecordPatientResponse;
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
@JsonInclude(Include.NON_NULL)
public class RecordPatientReport {

    private CardPatientResponse cardPatient;
    private Long countRecordForTime;
    private List<RecordPatientResponse> recordPatients;
    
}
