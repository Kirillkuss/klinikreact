package com.itrail.klinikreact.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.itrail.klinikreact.redis.model.UserSession;

@Configuration
@EnableCaching
@ComponentScan("com.itrail.klinikreact.redis")
@PropertySource(value = { "classpath:redis.properties" })
@EnableRedisRepositories( basePackages = "com.itrail.klinikreact.redis.repository")
public class RedisConfig {

    @Autowired
    private Environment env;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                                     redisStandaloneConfiguration.setHostName( env.getProperty("spring.redis.host" ));
                                     redisStandaloneConfiguration.setPort( Integer.parseInt( env.getProperty("spring.redis.port" )));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ReactiveRedisConnection reactiveRedisConnection(final ReactiveRedisConnectionFactory redisConnectionFactory) {
        return redisConnectionFactory.getReactiveConnection();
    }

    @Bean
    public ReactiveRedisOperations<String, Object> redisOperations(ReactiveRedisConnectionFactory factory) {
        final RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        final RedisSerializationContext<String, Object> context = builder.value(new JdkSerializationRedisSerializer()).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, UserSession> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, UserSession> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, UserSession> context = builder.value(new Jackson2JsonRedisSerializer<>(UserSession.class)).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
