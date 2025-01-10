package com.itrail.klinikreact.testcontainer;

import static org.instancio.Select.field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.itrail.klinikreact.models.Document;
import com.itrail.klinikreact.services.model.DocumentService;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
/**
 * Должен быть запущен Redis
 */
@Disabled
@Testcontainers
@SpringBootTest
@DisplayName("Тестирование сервиса DocumentService в тестконтейрнере PostgreSQLContainer")
public class PostgresContainerDocument {

    @SuppressWarnings("resource")
    @Container
    private static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                                                                        .withDatabaseName("Klinika")
                                                                        .withUsername("postgres")
                                                                        .withPassword("admin")
                                                                        .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres_test"));

    private final List<Document> listOne = new ArrayList<>();
    private final List<Document> listTwo = new ArrayList<>();

    @Autowired private DocumentService documentService;

    public static void initDataBase() {
        try {
            DatabaseClient.create( new TestConnectDataBase().connectionFactory() )
                          .sql( new String( Files.readAllBytes( Paths.get("src/main/resources/db/init_db.sql" ))))
                          .then()
                          .block(); 
        } catch (Exception e) {
            System.out.println("ERROR >>> " + e.getMessage());
        }
    }

    @BeforeAll
    public static void setUpClass() {
        postgresSQLContainer.start();
        initDataBase();
    }

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
    @Order(1)
    @DisplayName("Добавление документа")
    public void testAddDocument(){
        for (int i = 0; i < 20; i++) {
            documentService.addDocument(getDocument()).block();
        }
    }

    @ParameterizedTest
    @CsvSource({"1, 10"})
    @Order(2)
    @DisplayName("Ленивая загрузка документов")
    public void testGetLazyDocuments(int page, int size ){
        documentService.getLazyDocuments( page, size )
                       .flatMap( t ->{
                            listOne.add( t );
                            return Flux.just( t );
                        }).blockLast();
        documentService.getLazyDocuments( page, size )
                       .flatMap( t ->{
                            listTwo.add( t );
                            return Flux.just( t );
                        }).blockLast();
        assertEquals( listOne, listTwo );
        
    }

    @ParameterizedTest
    @CsvSource({"1, 1, 10"})
    @Order(3)
    @DisplayName("Поиск документов по параметрам")
    public void testFindByParam( String param, int page, int size ){
        documentService.findByParam( param, page, size )
                       .flatMap( t ->{
                            listOne.add( t );
                            return Flux.just( t );
                        }).blockLast();
        documentService.findByParam( param, page, size )
                       .flatMap( t ->{
                            listTwo.add( t );
                            return Flux.just( t );
                         }).blockLast();
        assertEquals( listOne, listTwo );
    }

    @Test
    @Order(4)
    @DisplayName("Добавление документа второй вариант")
    public void testAddDocumentTwo(){
        documentService.addDocument2( getDocument() );
    }

    @DisplayName("Генерация документа")
    public Document getDocument(){
        Document document = Instancio.of(Document.class)
                                     .generate(field(Document::getTypeDocument),  gen -> gen.oneOf("Паспорт", "Водительское уд.", "Свид. о рождении"))
                                     .generate(field(Document::getSeria),  gen -> gen.oneOf("TS", "AB", "AM", "CR"))
                                     .generate(field(Document::getNumar),  gen -> gen.text().pattern("#d#d#d#d#d#d#d#d#d"))
                                     .generate(field(Document::getSnils ),  gen -> gen.text().pattern("#d#d#d-#d#d#d-#d#d#d-#d#d"))
                                     .generate(field(Document::getPolis ),  gen -> gen.text().pattern("#d#d#d#d #d#d#d#d #d#d#d#d #d#d#d#d"))
                                     .ignore(Select.field( Document::getIdDocument )).create();
        return document;
    }

    
}
