package com.itrail.klinikreact.redis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.core.session.SessionDestroyedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan("com.itrail.klinikreact.redis")
@PropertySource(value = { "classpath:redis.properties" })
@EnableRedisRepositories( basePackages = "com.itrail.klinikreact.redis.repository")

public class RedisConfig {

    @EventListener
    public void handleSessionDestroyed( SessionDestroyedEvent event) {
        log.info( "Session finished" + event.getId());
    }
    
}
