package com.github.heliommsfilho.foodapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import com.github.heliommsfilho.foodapi.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource({"/application-test.properties"})
public class CadastroCozinhaIT {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private DatabaseCleaner databaseCleaner;
    
    @Autowired
    private CozinhaRepository cozinhaRepository;
    
    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";
        
        databaseCleaner.clearTables();
        prepararDados();
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
                .body("", Matchers.hasSize(2))
                .body("nome", Matchers.hasItems("Tailandesa", "Americana"));
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
    
    private void prepararDados() {
        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("Tailandesa");
        
        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        
        cozinhaRepository.saveAll(Arrays.asList(cozinha1, cozinha2));
    }
}
