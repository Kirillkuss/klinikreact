package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table( name = "rehabilitation_solution")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RehabilitationSolution implements Serializable{

    @Id
    @Column( name = "id_rehabilitation_solution")
    @Schema( name        = "idRehabilitationSolution",
             description = "ИД реабилитационного лечения",
             example     = "100",
             required    = true )
    private Long idRehabilitationSolution;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Наименование",
             example     = "Кинезитерапия1",
             required    = true )
    private String name;

    @Column( name = "survey_plan")
    @Schema( name        = "surveyPlan",
             description = "План обследования",
             example     = "План реабилитационного лечения",
             required    = true )
    private String surveyPlan;
    
}
