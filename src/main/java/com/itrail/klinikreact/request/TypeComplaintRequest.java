package com.itrail.klinikreact.request;

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
public class TypeComplaintRequest {
    
    @Schema(example = "1", name = "idCardPatient")
    private Long idCardPatient;
    @Schema(example = "2", name = "idTypeComplaint")
    private Long idTypeComplaint;
}
