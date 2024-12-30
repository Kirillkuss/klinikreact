package com.itrail.klinikreact.testcontainer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.r2dbc.spi.ConnectionFactory;

@Disabled
@Testcontainers
@SpringBootTest
public class RedisContainer {

    @SuppressWarnings("resource")
    @Container
    private static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);
            //.withCreateContainerCmdModifier(cmd -> cmd.withName("redis_test"));

    @BeforeAll
    public static void setUpClass() throws Exception {
        System.out.println("Redis is running on: " + redisContainer.getHost() + ":" + redisContainer.getMappedPort(6379));
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @TestConfiguration
    static class TestConnectDataBase {
        @Bean
        public LettuceConnectionFactory redisConnectionFactory() {
            int redisPort = redisContainer.getMappedPort(6379);
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(redisContainer.getHost());
            redisStandaloneConfiguration.setPort(redisPort);
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        }

        @Bean
        public ReactiveRedisConnection reactiveRedisConnection(final ReactiveRedisConnectionFactory redisConnectionFactory) {
            return redisConnectionFactory.getReactiveConnection();
        }
    }

    @Test
    public void testFirst() {
        
        System.out.println("Test is running");
    }
    
}
