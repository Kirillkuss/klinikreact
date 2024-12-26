package com.itrail.klinikreact.redis.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash("UserSession")
public class UserSession {

    @Id
    private String username;
    private String sessionId;
    
    @TimeToLive
    @Schema( example = "300")
    private Integer time;

    
}
