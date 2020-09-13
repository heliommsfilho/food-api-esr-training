package com.github.heliommsfilho.foodapi.jpa;

import com.github.heliommsfilho.foodapi.FoodApiApplication;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ConsultaRestauranteMain {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE).run(args);

        RestauranteRepository restauranteRepository = context.getBean(RestauranteRepository.class);
        restauranteRepository.todas().forEach(restaurante -> System.out.println(restaurante.getNome()));

        Restaurante restaurante = restauranteRepository.buscar(1L);
        System.out.printf("%d - %s - %s", restaurante.getId(), restaurante.getNome(), restaurante.getCozinha().getNome());
    }
}
