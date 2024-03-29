package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.RestauranteInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.RestauranteModelAssembler;
import com.github.heliommsfilho.foodapi.domain.exception.CidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.CozinhaNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroRestauranteService;
import com.github.heliommsfilho.foodapi.model.RestauranteModel;
import com.github.heliommsfilho.foodapi.model.input.RestauranteInput;
import com.github.heliommsfilho.foodapi.model.view.RestauranteView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
        List<RestauranteModel> restaurantes = restauranteRepository.findAll().stream().map(r -> modelMapper.map(r, RestauranteModel.class)).collect(Collectors.toList());
        final MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantes);
    
        final Class<?> view = projecao == null ? RestauranteView.Resumo.class
                                               : switch (projecao) {
                                                    case "apenas-nome" -> RestauranteView.ApenasNome.class;
                                                    case "resumo" -> RestauranteView.Resumo.class;
                                                    default -> null; /* Sem projeção . */
        };
        
        restaurantesWrapper.setSerializationView(view);
        return restaurantesWrapper;
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
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }
    
    @PutMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable final Long restauranteId) {
        cadastroRestauranteService.ativar(restauranteId);
    }
    
    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
        cadastroRestauranteService.ativar(restauranteIds);
    }
    
    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
        cadastroRestauranteService.inativar(restauranteIds);
    }
    
    @DeleteMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable final Long restauranteId) {
        cadastroRestauranteService.inativar(restauranteId);
    }
    
    @PutMapping("/{restauranteId}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(@PathVariable Long restauranteId) {
        cadastroRestauranteService.abrir(restauranteId);
    }
    
    @PutMapping("/{restauranteId}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(@PathVariable Long restauranteId) {
        cadastroRestauranteService.fechar(restauranteId);
    }
}
