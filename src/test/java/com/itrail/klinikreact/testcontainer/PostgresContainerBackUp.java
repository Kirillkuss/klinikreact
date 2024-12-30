package com.itrail.klinikreact.testcontainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.itrail.klinikreact.models.Document;
import com.itrail.klinikreact.services.model.DocumentService;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@Testcontainers
@SpringBootTest
@DisplayName("Тестирование контейнера PostgreSQLContainer с загрузкой БД из backup")
public class PostgresContainerBackUp {

    @SuppressWarnings("resource")
    @Container
    private static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                                                                        .withDatabaseName("Klinika")
                                                                        .withUsername("postgres")
                                                                        .withPassword("admin");

    private final List<Document> listOne = new ArrayList<>();
    private final List<Document> listTwo = new ArrayList<>();

    @BeforeAll
    public static void setUpClass() throws Exception{
        postgresSQLContainer.start();
        //загрузка backup
        getRestoreDataBase( postgresSQLContainer.getMappedPort( PostgreSQLContainer.POSTGRESQL_PORT ));
    }

    @Autowired private DocumentService documentService;

    @AfterAll
    public static void tearDownClass() {
        postgresSQLContainer.stop();
    }

    @TestConfiguration
    static class TestConnectDataBase {

        @Bean
        public ConnectionFactory connectionFactory() {
            return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                    .host(postgresSQLContainer.getHost())
                    .port(postgresSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                    .username(postgresSQLContainer.getUsername())
                    .password(postgresSQLContainer.getPassword())
                    .database(postgresSQLContainer.getDatabaseName())
                    .build());
        }

        @Bean
        public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
            return new R2dbcEntityTemplate(connectionFactory);
        }

    }

    @Test
    @DisplayName( "Ленивая загрузка")
    public void testGetLazyDocuments(){
        documentService.getLazyDocuments(1, 10)
                       .flatMap( t -> {
                            listOne.add( t );
                            return Flux.just( t );
                        }).blockLast();
        documentService.getLazyDocuments(1, 10)
                       .flatMap( t -> {
                            listTwo.add( t );
                            return Flux.just( t );
                        }).blockLast();
        assertEquals( listOne, listTwo );
    }

    @Test
    @DisplayName( "Поиск по параметрам")
    public void testFindByParam(){
        documentService.findByParam( "1", 1, 10 )
                       .flatMap( t -> {
                            listOne.add( t );
                            return Flux.just( t );
                        }).blockLast();
        documentService.findByParam( "1", 1, 10 )
                       .flatMap( t -> {
                            listTwo.add( t );
                            return Flux.just( t );
                        }).blockLast();
        assertEquals( listOne, listTwo );
    }

    @DisplayName("Загрузка данных в бд")
    public static void getRestoreDataBase( int port ) throws Exception {
        List<String> restore = List.of("pg_restore", "--host=localhost", "--port=" + port,"--username=postgres",
                                       "--dbname=Klinika",  "--no-password","--format=c","--verbose",
                                          "./src/main/resources/db/backup/klinika.backup");
        processBuilder( restore );
    }
    
    @DisplayName("Загрузка данных")
    private static void processBuilder( List<String>  commnds ) throws Exception{
        ProcessBuilder processBuilder = new ProcessBuilder( commnds );
        processBuilder.environment().put("PGPASSWORD", "admin");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Restore Db success!");
        } else {   
            System.out.println("Code >>> " + exitCode);
        }

    }
    
}
