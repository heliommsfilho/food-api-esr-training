package com.github.heliommsfilho.foodapi.jpa;

import com.github.heliommsfilho.foodapi.FoodApiApplication;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class CadastroCozinhaMain {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE).run(args);

        CozinhaRepository cozinhaRepository = context.getBean(CozinhaRepository.class);

        Cozinha c1 = new Cozinha();
        Cozinha c2 = new Cozinha();

        c1.setNome("Brasileira");
        c2.setNome("Japonesa");

        cozinhaRepository.salvar(c1);
        cozinhaRepository.salvar(c2);

        System.out.printf("%d - %s", c1.getId(), c1.getNome());
        System.out.printf("%d - %s", c2.getId(), c2.getNome());

        // Atualizar cozinha
        Cozinha cozinha = new Cozinha();
        cozinha.setId(1L);
        cozinha.setNome("Tailandesa (Atualizada)");

        cozinhaRepository.salvar(cozinha);

        // Remover
        Cozinha cozinha1 = cozinhaRepository.buscar(2L);
        cozinhaRepository.remover(cozinha1);
    }
}
