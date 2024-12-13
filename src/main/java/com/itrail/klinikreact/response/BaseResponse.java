package com.itrail.klinikreact.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BaseResponse<T> {

    @Schema (description = "Код сообщения", name = "Код сообщения",  example = "200")
    private Integer code = 0;
    @Schema (description = "Сообщение", name = "Сообщение",  example = "успешно")
    private String message = "успешно";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema (description = "Ответ", name = "response")
    private T response;

    public BaseResponse(){
    }

    public BaseResponse(Integer code, String massage){
        this.code = code;
        this.message = massage;
    }

    public BaseResponse(Integer code, String message, T response){
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public static BaseResponse success() {
        return new BaseResponse( 200, "success");
    }
    
    public static BaseResponse error( Integer code, Throwable e ){
        return new BaseResponse( code , null == e.getMessage() ? "Сообщение об ошибке" : e.getMessage());
    }

}