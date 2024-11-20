package com.itrail.klinikreact.request.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @Schema( name = "username",
             description = "username",
             example = "admin")
    private String username;

    @Schema( name = "password",
             description = "password",
             example = "admin")
    private String password;
}