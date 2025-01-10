package com.itrail.klinikreact.testcontainer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
/**
 * свойства в application.properties and redis properties
 * подключение к ресурсам
 * application.properties: r2dbc:postgresql://host.docker.internal:5434/Klinika
 * redis.application: spring.redis.host=redisreact
 * создание контейнера klinikreact:latest с учетом этих свойств ( build project )
 */
@Disabled
@Testcontainers
@DisplayName("Тестирование прилоджения при условии что есть контейнеры в докере для Redis and Postgres и запущены")
public class KlinikReactContainer {

    private static String sessionId;

    @SuppressWarnings("resource")
    @Container
    private static GenericContainer<?> klinikreactContainer = new GenericContainer<>("klinikreact:latest")
                                                                .withExposedPorts(8086 )
                                                                .withNetworkMode("klinikreact_klinikreact-es")
                                                                .withCreateContainerCmdModifier(cmd -> cmd.withName("klinikreact_test" ));

    @BeforeAll
    public static void setUpClass() throws Exception {
        klinikreactContainer.start();
        RestAssured.baseURI = "http://localhost:" + klinikreactContainer.getMappedPort(8086 );
        authSystem();
        
    }

    @AfterAll
    public static void tearDownClass() throws InterruptedException {
        Thread.sleep( 15000 );
        klinikreactContainer.stop();
    }
    

    @Test
    @DisplayName("Вызоз конечной точки для получения кол-ва врачей")
    public void testGetDoctors(){
        try{
            Response response = given().contentType(ContentType.JSON)
                                    .cookie("SESSION", sessionId ) 
                                    .when()
                                    .get("/react/doctors/count")
                                    .then()
                                    .extract().response(); 
           System.out.println("Count Doctors: " + response.asString());  
        }catch( Exception ex ){   
        }
    }

    @Test
    @DisplayName("Вызоз конечной точки для получения кол-ва пациентов")
    public void testGetPatients(){
        try{
            Response response = given().contentType(ContentType.JSON)
                                    .cookie("SESSION", sessionId ) 
                                    .when()
                                    .get("/react/patients/count")
                                    .then()
                                    .extract().response(); 
           System.out.println("Count Patients: " + response.asString());  
        }catch( Exception ex ){   
        }
    }

    

    @Test
    @DisplayName("Вызоз конечной точки для получения кол-ва документов")
    public void testGetDocuments() throws InterruptedException{
        try{
            Response response = given().contentType(ContentType.JSON)
                                    .cookie("SESSION", sessionId ) 
                                    .when()
                                    .get("/react/documents/count")
                                    .then()
                                    .extract().response(); 
           System.out.println("Count Documents: " + response.asString());  
        }catch( Exception ex ){   
        }
    }

    @DisplayName("авторизация в системе")
    public static void authSystem(  ){
        try {
            Response response = given().contentType(ContentType.URLENC)
                                       .formParam("username", "user")
                                       .formParam("password", "user") 
                                       .when()
                                       .post("/react/login")
                                       .then()
                                       .extract().response();  
            sessionId = response.getCookie("SESSION");
        } catch (Exception ex) {
            System.out.println( ex.getMessage() );
        }
    }

}
