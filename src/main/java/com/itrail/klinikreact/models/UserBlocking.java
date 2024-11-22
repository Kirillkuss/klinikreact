package com.itrail.klinikreact.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "user_blocking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserBlocking implements Serializable{

    @Id
    @Column( name = "id_block")
    @Schema( name        = "idBlockUser",
             description = "ИД блокировки",
             example     = "100",
             required    = true )
    private Long idBlock;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column( name = "date_block")
    @Schema( name        = "dateBlock",
             description = "Дата и время блокировки",
             example     = "2023-01-22 18:00:00.745",
             required    = true )
    private LocalDateTime dateBlock;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column( name = "date_plan_unblock")
    @Schema( name        = "datePlanUnblock",
             description = "Дата и время планируемой разблокировки",
             example     = "2023-07-22 18:00:00.745",
             required    = true )
    private LocalDateTime datePlanUnblock;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column( name = "date_unblock")
    @Schema( name        = "dateUnblock",
             description = "Дата и время разблокировки",
             example     = "2023-07-22 18:00:00.745",
             required    = true )
    private LocalDateTime dateUnblock;


    @Column( name = "status")
    @Schema( name        = "status",
             description = "статус",
             example     = "true",
             required    = true )
    private Boolean status;

    @Column( name = "status_block")
    @Schema( name        = "statusBlock",
             description = "Статус блокировки",
             example     = "1",
             required    = true )
    private Integer statusBlock; 

    @Column( name = "user_id")
    @Schema( name        = "userId",
             description = "Ид пользователя",
             example     = "1",
             required    = true )
    private Long userId;
    
}
