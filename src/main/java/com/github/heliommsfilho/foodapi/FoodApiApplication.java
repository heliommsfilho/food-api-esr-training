package com.github.heliommsfilho.foodapi;

import com.github.heliommsfilho.foodapi.infrastructure.repository.CustomJPARepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJPARepositoryImpl.class) // Substitui SimpleJpaRepository
public class FoodApiApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(FoodApiApplication.class, args);
    }

}
