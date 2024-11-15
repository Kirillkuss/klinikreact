package com.itrail.klinikreact.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table( name = "key_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class KeyEntity implements Serializable {
    
    @Id
    @Column( name = "id_key")
    @Schema( name        = "idKeyEntity",
             description = "Ид ключа",
             example     = "100",
             required    = true )
    @JsonInclude(Include.NON_NULL) 
    private Long idKeyEntity;

    @Column( name = "key_alice")
    private String alice;

    @Column( name = "date_create")
    @Schema( name         = "dateCreate",
             description  = "dateCreate",
             required     = true )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateCreate;

    @Column( name = "key_public")
    @Schema( name        = "publicKey",
             description  = "publicKey",
             example      = "jdbjsfkwehwiuy782342iuhsifhsdifw",
             required     = true )
    private String publicKey;
    
    @Column( name = "key_private")
    @Schema( name         = "privateKey",
             description  = "privateKey",
             example      = "jdbjsfkwehwiuy782342iuhsifhsdifw",
             required     = true )
    private String privateKey;
}
