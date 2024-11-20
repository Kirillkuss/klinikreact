package com.itrail.klinikreact.request;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRequest {

    @Column( name = "login")
    @Schema( name        = "login",
             description = "admin",
             example     = "admin",
             required    = true )
    @NotNull         
    private String login;

    @Column( name = "password")
    @Schema( name        = "password",
             description = "admin",
             example     = "admin",
             required    = true )
    @NotNull
    private String password;

    @Column( name = "role")
    @Schema( name        = "role",
             description = "роль",
             example     = "ADMIN",
             required    = true )
    private String role;

    
    @Column( name = "email")
    @Schema( name        = "email",
             description = "email",
             example     = "jseuertne@mail.com",
             required    = true )
    @NotNull
    private String email;
    
}