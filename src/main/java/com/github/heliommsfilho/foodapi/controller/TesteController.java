package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teste")
public class TesteController {

    private final CozinhaRepository cozinhaRepository;

    @Autowired
    public TesteController(CozinhaRepository cozinhaRepository) {
        this.cozinhaRepository = cozinhaRepository;
    }

/*    @GetMapping("/cozinhas/por-nome")
    public List<Cozinha> cozinhasPorNome(@RequestParam String nome) {
        return cozinhaRepository.consultarPorNome(nome);
    }*/
}
