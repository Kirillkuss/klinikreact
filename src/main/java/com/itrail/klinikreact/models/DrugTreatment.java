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

@Table( name = "drug_treatment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude( Include.NON_NULL )
public class DrugTreatment implements Serializable {
    
    @Id
    @Hidden
    @Column( name = "id_drug_treatment")
    @Schema( name        = "idDrug",
             description = "ИД медикаментозного лечения",
             example     = "100",
             required    = true )

    private Long idDrugTreatment;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Наименование",
             example     = "Кортикостероиды",
             required    = true )
    private String name;
}
