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

@Table( name = "type_complaint")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(Include.NON_NULL)
public class TypeComplaint implements Serializable {

    @Id
    @Hidden 
    @Column (name = "id_type_complaint")
    @Schema( name        = "idTypeComplaint",
             description = "ИД поджалобы",
             example     = "100",
             required    = true )
    private Long idTypeComplaint;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Наименование поджалобы",
             example     = "Парапарезы",
             required    = true )
    private String name;

    @Column( name = "complaint_id")
    @Schema( name        = "complaintId",
            description = "ИД жалобы",
            example     = "1",
            required    = true )
    private Long complaintId;
    
}
