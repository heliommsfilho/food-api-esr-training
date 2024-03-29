package com.github.heliommsfilho.foodapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import com.github.heliommsfilho.foodapi.util.DatabaseCleaner;
import com.github.heliommsfilho.foodapi.util.ResourceUtils;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource({"/application-test.properties"})
public class CadastroCozinhaIT {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private DatabaseCleaner databaseCleaner;
    
    @Autowired
    private CozinhaRepository cozinhaRepository;
    
    private static final int COZINHA_ID_INEXISTENTE = 100;
    
    private Cozinha cozinhaAmericana;
    private int quantidadeCozinhasCadastradas;
    private String jsonCorretoCozinhaChinesa;
    
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
    public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
        
        RestAssured.given()
                .accept(ContentType.JSON)
            .when().get()
            .then()
                .body("", Matchers.hasSize(quantidadeCozinhasCadastradas));
    }
    
    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha() throws Exception {
        RestAssured.given()
                .body(ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.CREATED.value());
    }
    
    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
        RestAssured.given()
                .pathParam("cozinhaId", cozinhaAmericana.getId())
                .accept(ContentType.JSON)
                .when().get("/{cozinhaId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(cozinhaAmericana.getNome()));
    }
    
    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
        RestAssured.given()
                .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
                .accept(ContentType.JSON)
                .when().get("/{cozinhaId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    
    private void prepararDados() {
        Cozinha cozinhaTailandesa = new Cozinha();
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);
    
        cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);
    
        quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
    }
}
