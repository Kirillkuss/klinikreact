package com.itrail.klinikreact.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itrail.klinikreact.models.Complaint;
import com.itrail.klinikreact.models.TypeComplaint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude( Include.NON_NULL )
public class TypeComplaintResponse {

    private Complaint complaint;
    private List<TypeComplaint> typeComplaints;
}
