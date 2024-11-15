package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;
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

@Table( name = "patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude( Include.NON_NULL )
public class Patient implements Serializable {
    
    @Id
    @Hidden
    @Column( name = "id_patient")
    @Schema( name        = "idPatient",
             description = "ИД пациента",
             example     = "100",
             required    = true )
    private Long idPatient;

    @Column( name = "surname")
    @Schema( name        = "surname",
             description = "Фамилия",
             example     = "Пупкин",
             required    = true )
    private String surname;

    @Column( name = "name")
    @Schema( name        = "name",
             description = "Имя",
             example     = "Михаил",
             required    = true )
    private String name;

    @Column( name = "full_name")
    @Schema( name        = "fullName",
             description = "Отчество",
             example     = "Петрович",
             required    = true )
    private String fullName;

    @Column( name = "gender")
    @Schema( name        = "gender",
            description = "Пол пациента",
            example     = "0",
            required    = true )
    private String gender;

    @Column( name = "phone")
    @Schema( name        = "phone",
             description = "Номер телефона",
             example     = "+375295618456",
             required    = true )
    @Pattern( regexp = "^\\+375(29|33|25|44)\\d{7}$", message = "Неверный формат номера телефона. Должно быть +375(29|33|25|44) и 7 цифр.")
    private String phone;

    @Column( name = "address")
    @Schema( name        = "address",
             description = "Адрес",
             example     = "Спб, Проспект Тихорецкого д.5",
             required    = true )
    private String address;

    /**@Hidden
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "document_id", referencedColumnName = "id_document" ) 
    private Document document;*/
    @Hidden
    @Column( name = "document_id")
    @Schema( name        = "documentId",
            description = "documentId",
            example     = "3009" )
    private Long documentId;


}
