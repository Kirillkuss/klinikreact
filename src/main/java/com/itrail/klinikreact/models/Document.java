package com.itrail.klinikreact.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Document implements Serializable {
    
    @Id
    @Hidden
    @Column( name = "id_document")
    @Schema( name        = "idDocument",
             description = "ИД документа",
             example     = "100",
             required    = true )
    private Long idDocument;

    @Column( name = "type_document")
    @Schema( name        = "typeDocument",
             description = "Тип документа",
             example     = "Паспорт",
             required    = true )
    private String typeDocument;

    @Column( name = "seria")
    @Schema( name        = "seria",
             description = "Серия документа",
             example     = "ВМ",
             required    = true )
    @Size( min = 2, max = 2, message = "Серия документа должна содержать ровно 2 символа" )
    @Pattern(regexp = "^[A-Z]{2}$", message = "Серия документа должна состоять только из двух заглавных букв" )
    private String seria;

    @Column( name = "numar")
    @Schema( name        = "numar",
             description = "Номер документа",
             example     = "123243455",
             required    = true )
    @Size( min = 9, max = 12, message = "Номер документа должен содержать от 9 до 12 символов" )
    private String numar;

    @Column( name = "snils")
    @Schema( name        = "snils",
             description = "СНИЛС",
             example     = "123-456-789-01",
             required    = true )
    @Pattern( regexp = "^\\d{3}-\\d{3}-\\d{3}-\\d{2}$", message = "Некорректный формат для СНИЛСа: ###-###-###-##" )
    private String snils;

    @Column( name = "polis")
    @Schema( name        = "polis",
             description = "Полис",
             example     = "0000 0000 0000 0000",
             required    = true )
    @Pattern( regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$", message = "Некорректный формат для ПОЛИСа: #### #### #### ####" )
    private String polis;
}
