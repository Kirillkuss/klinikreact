package com.itrail.klinikreact.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrail.klinikreact.redis.model.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
@Configuration
@RequiredArgsConstructor
public class SessionExpiredEventListener {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<String> event) {
        String eventId = new String(event.getId());
        Object value = event.getValue();
        if (value instanceof UserSession) {
            UserSession userSession = ( UserSession ) value; 
            log.info("1 Key {} has expired. UserSession: {}", eventId, userSession);
        } else {
            log.info("2 Key {} has expired. Value: {}", eventId, value);
        }
    }

}