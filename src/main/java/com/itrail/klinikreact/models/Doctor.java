package com.itrail.klinikreact.models;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

@Table(name = "doctor")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Doctor implements Serializable {
    
    @Id
    @Hidden
    @Column( name = "id_doctor")
    @Schema( name        = "idDoctor",
             description = "ИД доктора",
             example     = "1",
             required    = true )
    @JsonInclude(Include.NON_NULL)
    private Long idDoctor;
    
    @Column( name = "surname")
    @Schema( name        = "surname",
             description = "Фамилия",
             example     = "Петров",
             required    = true )
    private String surname;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Имя",
             example     = "Петр",
             required    = true )
    private String name;

    @Column( name = "full_name")
    @Schema( name        = "fullName",
             description = "Отчество",
             example     = "Петрович",
             required    = true )
    private String fullName;
}
