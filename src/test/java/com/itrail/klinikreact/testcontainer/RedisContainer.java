package com.itrail.klinikreact.testcontainer;

import static org.instancio.Select.field;

import java.util.ArrayList;
import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.itrail.klinikreact.redis.model.UserSession;
import com.itrail.klinikreact.redis.repository.UserSessionRepository;
import com.itrail.klinikreact.response.UserResponse;
import com.itrail.klinikreact.services.model.UserService;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@Testcontainers
@SpringBootTest
@DisplayName("Тестирование контейнеров Postgres and Redis в testcontainer")
public class RedisContainer {

    @SuppressWarnings("resource")
    @Container
    private static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
                                                                .withExposedPorts(6379)
                                                                .withCreateContainerCmdModifier(cmd -> cmd.withName("redis_test"));

    @SuppressWarnings("resource")
    @Container
    private static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                                                                        .withDatabaseName("Klinika")
                                                                        .withUsername("postgres")
                                                                        .withPassword("admin")
                                                                        .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres_test"));


    @BeforeAll
    public static void setUpClass() throws Exception {
        redisContainer.start();
        postgresSQLContainer.start();
        BackupPostgres.getRestoreDataBase( postgresSQLContainer.getMappedPort( PostgreSQLContainer.POSTGRESQL_PORT ));
    }

    @AfterAll
    public static void tearDownClass() throws InterruptedException {
        /**
         * Для работы @EventListener
         */
        Thread.sleep( 6000 );
        postgresSQLContainer.stop();
        redisContainer.stop();
    }
    
    @Autowired private UserSessionRepository userSessionRepository;
    @Autowired private UserService userService;

    private List<UserResponse> firstList = new ArrayList<>();
    private List<UserResponse> secondList = new ArrayList<>();

    @TestConfiguration
    static class TestConnectDataBase {
        /**
         * Connect with redis_test
         * @return
         */
        @Bean
        public LettuceConnectionFactory redisConnectionFactory() {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                                         redisStandaloneConfiguration.setHostName( redisContainer.getHost() );
                                         redisStandaloneConfiguration.setPort( redisContainer.getMappedPort(6379 ));
            return new LettuceConnectionFactory( redisStandaloneConfiguration );
        }

        @Bean
        public ReactiveRedisConnection reactiveRedisConnection(final ReactiveRedisConnectionFactory redisConnectionFactory) {
            return redisConnectionFactory.getReactiveConnection();
        }

        /**
         * Connect with postgres_test
         * @return
         */
        @Bean
        public ConnectionFactory connectionFactory() {
            return new PostgresqlConnectionFactory( PostgresqlConnectionConfiguration.builder()
                                                                                     .host( postgresSQLContainer.getHost() )
                                                                                     .port( postgresSQLContainer.getMappedPort( PostgreSQLContainer.POSTGRESQL_PORT ))
                                                                                     .username( postgresSQLContainer.getUsername() )
                                                                                     .password( postgresSQLContainer.getPassword() )
                                                                                     .database( postgresSQLContainer.getDatabaseName() )
                                                                                     .build() );
        }

        @Bean
        public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
            return new R2dbcEntityTemplate( connectionFactory );
        }
    }

    @Test
    @Order(1)
    @DisplayName("Сохраение сессий")
    public void testSaveUserSession() {
        for (int i = 0; i < 20; i++) {
            userSessionRepository.save( getUserSession() );
        }
    }

    @Test
    @Order(2)
    @DisplayName("кол-во сессий и список сессий")
    public void testFindAllUserSession(){
        System.out.println( "COUNT : " + userSessionRepository.count() );
        System.out.println( "LIST : " + userSessionRepository.findAll() );
    }

    @Test
    @Order(3)
    @DisplayName("Получение списка пользователей")
    public void testFindUsers(){
        userService.getUsers().flatMap( user ->{
            firstList.add( user );
            return Flux.empty();
        }).blockLast();
        userService.getUsers().flatMap( user ->{
            secondList.add( user );
            return Flux.empty();
        }).blockLast();
        assertEquals( firstList.toString(), secondList.toString());
    }

    @DisplayName("Генерация документа")
    public UserSession getUserSession(){
        UserSession userSession = Instancio.of(UserSession.class)
                                     .generate(field(UserSession::getSessionId),  gen -> gen.text().pattern("#d#d#d#d#d#d#d#d#d"))
                                     .generate(field(UserSession::getUsername ),  gen -> gen.text().pattern("#d#d#d#d#d#d#d#d#d#d#d"))
                                     .generate(field(UserSession::getTime ),  gen -> gen.oneOf( 3)).create();
        return userSession;
    }
}
