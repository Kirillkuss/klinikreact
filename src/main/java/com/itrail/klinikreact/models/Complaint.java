package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table( name = "complaint")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Complaint implements Serializable{

    @Id
    @Hidden
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "id_complaint")
    @Schema( name        = "idComplaint",
             description = "ИД жалобы",
             example     = "100",
             required    = true )
    @JsonInclude(Include.NON_NULL)        
    private Long idComplaint;

    @Column( name = "functional_impairment")
    @Schema( name        = "functionalImpairment",
             description = "Наименование жалобы",
             example     = "Симптомы поражения пирамидного тракта",
             required    = true )
    private String functionalImpairment;


}
