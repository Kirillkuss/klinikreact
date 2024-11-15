package com.itrail.klinikreact.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseError {

    @Schema ( description = "Код сообщения",
              name = "code",
              example = "400")
    private int code;

    @Schema ( description = "Сообщение",
             name = "message",
             example = "Плохой запрос")
    private String message;
    
}
