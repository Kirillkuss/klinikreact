package com.itrail.klinikreact.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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

@Table( name = "kl_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements Serializable{

    @Id
    @Hidden
    @Column( name = "id")
    @Schema( name        = "id",
             description = "Ид пользователя",
             example     = "100" )
    private Long id;

    @Column( name = "login")
    @Schema( name        = "login",
             description = "Логин",
             example     = "admin",
             required    = true )
    @NotNull         
    private String login;

    @Column( name = "password")
    @Schema( name        = "password",
             description = "Пароль",
             example     = "admin",
             required    = true )
    @NotNull
    private String password;

    @Column( name = "role")
    @Schema( name        = "role",
            example     = "1",
            description = "роль" )
    private String role;

    
    @Column( name = "email")
    @Schema( name        = "email",
             description = "email",
             example     = "jseuertne@mail.com",
             required    = true )
    @NotNull
    private String email;

    @Hidden
    @Schema( name        = "salt",
             description = "соль",
             example     = "sfdkj348724hisfs49h",
             required    = true )
    @Column( name = "salt")
    private String salt;

    @Column( name = "status")
    @Schema( name        = "status",
             description = "Статус блокировки",
             example     = "false",
             required    = true )
    private Boolean status;

}
