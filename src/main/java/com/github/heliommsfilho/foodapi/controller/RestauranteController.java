package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.RestauranteInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.RestauranteModelAssembler;
import com.github.heliommsfilho.foodapi.domain.exception.CozinhaNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroRestauranteService;
import com.github.heliommsfilho.foodapi.model.RestauranteModel;
import com.github.heliommsfilho.foodapi.model.input.RestauranteInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final CadastroRestauranteService cadastroRestauranteService;
    private final ModelMapper modelMapper;
    private final RestauranteModelAssembler assembler;
    private final RestauranteInputDisassembler disassembler;

    @Autowired
    public RestauranteController(RestauranteRepository restauranteRepository, CadastroRestauranteService cadastroRestauranteService, ModelMapper modelMapper,
                                 RestauranteModelAssembler assembler, RestauranteInputDisassembler disassembler) {
        this.restauranteRepository = restauranteRepository;
        this.cadastroRestauranteService = cadastroRestauranteService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
        this.disassembler = disassembler;
    }

    @GetMapping
    public ResponseEntity<List<RestauranteModel>> listar() {
        List<RestauranteModel> restaurantes = restauranteRepository.findAll().stream()
                                                                   .map(r -> modelMapper.map(r, RestauranteModel.class))
                                                                   .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        return restaurante.map(r -> ResponseEntity.ok(modelMapper.map(r, RestauranteModel.class)))
                          .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Restaurante adicionar(@RequestBody @Valid RestauranteInput restaurante) {
        try {
            return cadastroRestauranteService.salvar(modelMapper.map(restaurante, Restaurante.class));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(id);
            disassembler.copyToDomainObject(restauranteInput, restauranteAtual);
            return assembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }
}
