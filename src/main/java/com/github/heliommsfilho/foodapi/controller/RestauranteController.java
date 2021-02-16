package com.github.heliommsfilho.foodapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    public RestauranteController(RestauranteRepository restauranteRepository,
                                 CadastroRestauranteService cadastroRestauranteService) {
        this.restauranteRepository = restauranteRepository;
        this.cadastroRestauranteService = cadastroRestauranteService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        return ResponseEntity.ok(restauranteRepository.findAll());
    }

    @GetMapping("/por-nome")
    public ResponseEntity<List<Restaurante>> porNome(String nome, Long cozinhaId) {
        return ResponseEntity.ok(restauranteRepository.consultarPorNome(nome, cozinhaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        return restauranteOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Restaurante restaurante) {
        try {
            Restaurante restauranteSalvo = cadastroRestauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);

        if (restauranteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Restaurante restauranteAtual = restauranteOptional.get();
        BeanUtils.copyProperties(restaurante, restauranteAtual, "id");

        try {
            Restaurante restauranteAtualizado = cadastroRestauranteService.salvar(restauranteAtual);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

        if (restauranteAtual.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Restaurante restaurante = restauranteAtual.get();

        merge(campos, restaurante);

        return atualizar(restauranteId, restaurante);
    }

    private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino) {
        ObjectMapper mapper = new ObjectMapper();
        Restaurante restauranteOrigem = mapper.convertValue(camposOrigem, Restaurante.class);

        camposOrigem.forEach((propriedade, valor) -> {
            Optional<Field> field = Optional.ofNullable(ReflectionUtils.findField(Restaurante.class, propriedade));

            if (field.isPresent()) {
                field.get().setAccessible(true);

                Object novoValor = ReflectionUtils.getField(field.get(), restauranteOrigem);
                ReflectionUtils.setField(field.get(), restauranteDestino, novoValor);
            }
        });
    }
}
