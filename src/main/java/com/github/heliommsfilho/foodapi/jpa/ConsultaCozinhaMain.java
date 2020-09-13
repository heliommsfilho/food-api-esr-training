package com.github.heliommsfilho.foodapi.jpa;

import com.github.heliommsfilho.foodapi.FoodApiApplication;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ConsultaCozinhaMain {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE).run(args);

        CozinhaRepository cozinhaRepository = context.getBean(CozinhaRepository.class);
        cozinhaRepository.todas().forEach(cozinha -> System.out.println(cozinha.getNome()));

        Cozinha cozinha = cozinhaRepository.buscar(1L);
        System.out.printf("%d - %s (Find One)", cozinha.getId(), cozinha.getNome());
    }
}
