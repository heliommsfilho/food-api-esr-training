package com.github.heliommsfilho.foodapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource({"/application-test.properties"})
public class CadastroCozinhaIT {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private Flyway flyway;
    
    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";
        
        flyway.migrate();
    }
    
    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinhas() {
        
        RestAssured.given()
                .accept(ContentType.JSON)
            .when().get()
            .then().statusCode(HttpStatus.OK.value());
    }
    
    @Test
    public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
        
        RestAssured.given()
                .accept(ContentType.JSON)
            .when().get()
            .then()
                .body("", Matchers.hasSize(4))
                .body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
    }
    
    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha() throws Exception {
        final Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");
        
        RestAssured.given()
                .body((new ObjectMapper()).writeValueAsString(cozinha))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.CREATED.value());
    }
}
