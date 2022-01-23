package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.CozinhaInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.CozinhaModelAssembler;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeEmUsoException;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.repository.CozinhaRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroCozinhaService;
import com.github.heliommsfilho.foodapi.model.CozinhaModel;
import com.github.heliommsfilho.foodapi.model.input.CozinhaInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cozinhas", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;
    private final CadastroCozinhaService cadastroCozinha;
    private final CozinhaModelAssembler cozinhaModelAssembler;
    private final CozinhaInputDisassembler cozinhaInputDisassembler;

    @Autowired
    public CozinhaController(CozinhaRepository cozinhaRepository, CadastroCozinhaService cadastroCozinhaService, CozinhaModelAssembler cozinhaModelAssembler, CozinhaInputDisassembler cozinhaInputDisassembler) {
        this.cozinhaRepository = cozinhaRepository;
        this.cadastroCozinha = cadastroCozinhaService;
        this.cozinhaModelAssembler = cozinhaModelAssembler;
        this.cozinhaInputDisassembler = cozinhaInputDisassembler;
    }
    
    @GetMapping
    public Page<CozinhaModel> listar(@PageableDefault(size = 2) final Pageable pageable) {
        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
        
        final List<CozinhaModel> cozinhasModel =  cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent());
        return new PageImpl<>(cozinhasModel, pageable, cozinhasPage.getTotalElements());
    }
    
    @GetMapping("/{cozinhaId}")
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {
        Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
        
        return cozinhaModelAssembler.toModel(cozinha);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        cozinha = cadastroCozinha.salvar(cozinha);
        
        return cozinhaModelAssembler.toModel(cozinha);
    }
    
    @PutMapping("/{cozinhaId}")
    public CozinhaModel atualizar(@PathVariable Long cozinhaId,
                                  @RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
        
        return cozinhaModelAssembler.toModel(cozinhaAtual);
    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
        try {
            cadastroCozinha.excluir(cozinhaId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
