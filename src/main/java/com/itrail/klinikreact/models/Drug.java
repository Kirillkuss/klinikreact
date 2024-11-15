package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
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
public class Drug implements Serializable{
    
    @Id
    @Column( name = "id_dr")
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

    @Hidden
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "drug_id", referencedColumnName = "id_drug" )
    private DrugTreatment drugTreatment ;
}
