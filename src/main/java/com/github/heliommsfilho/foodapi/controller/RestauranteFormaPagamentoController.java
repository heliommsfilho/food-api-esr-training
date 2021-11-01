package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.FormaPagamentoModelAssembler;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.service.CadastroRestauranteService;
import com.github.heliommsfilho.foodapi.model.FormaPagamentoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {

    private final CadastroRestauranteService cadastroRestauranteService;
    private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;
    
    @Autowired
    public RestauranteFormaPagamentoController(CadastroRestauranteService cadastroRestauranteService,
                                               FormaPagamentoModelAssembler formaPagamentoModelAssembler) {
        this.cadastroRestauranteService = cadastroRestauranteService;
        this.formaPagamentoModelAssembler = formaPagamentoModelAssembler;
    }

    @GetMapping
    public ResponseEntity<List<FormaPagamentoModel>> listar(@PathVariable Long restauranteId) {
        final Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        
        return ResponseEntity.ok(formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento()));
    }
    
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        cadastroRestauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
    }
    
    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        cadastroRestauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
    }
}
