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

@Table( name = "card_patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude( Include.NON_NULL )
public class CardPatient implements Serializable {

    @Id
    @Hidden
    @Column( name = "id_card_patient")
    @Schema( name        = "idCardPatient",
             description = "ИД карты пациента",
             example     = "100",
             required    = true )
    private Long idCardPatient;

    @Column( name = "diagnosis")
    @Schema( name        = "diagnosis",
             description = "Диагноз пациента",
             example     = "Рассеянный склероз",
             required    = true )
    private String diagnosis;
    
    @Column( name = "allergy")
    @Schema( name        = "allergy",
             description = "Аллергия на препараты",
             example     = "true",
             required    = true )
    private Boolean allergy;
    
    @Column( name = "note")
    @Schema( name        = "note",
             description = "Примечание",
             example     = "Есть аллергия на цитрамон" )
    private String note;
    
    @Column( name = "сonclusion")
    @Schema( name        = "сonclusion",
             description = "Заключение",
             example     = "Болен")
    private String сonclusion;
    
    @Column( name = "patient_id")
    @Schema( name        = "patientId",
             description = "ИД пациента",
             example     = "1")
    private Long patientId;
    
}
