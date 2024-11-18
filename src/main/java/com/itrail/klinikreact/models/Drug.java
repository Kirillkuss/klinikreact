package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
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

@Table( name = "drug")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude( Include.NON_NULL )
public class Drug implements Serializable{
    
    @Id
    @Hidden
    @Column( name = "id_drug")
    @Schema( name        = "idDrug",
             description = "ИД лекарства",
             example     = "100",
             required    = true )
    private Long idDrug;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Препараты",
             example     = "Карвалол 2 чайные ложки в день",
             required    = true )
    private String name;

    @Column( name = "drug_treatment_id")
    @Schema( name        = "drugTreatmentId",
             description = "Ид мед лечения",
             example     = "1")
    private Long drugTreatmentId;

}
