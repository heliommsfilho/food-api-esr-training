package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.CidadeInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.CidadeModelAssembler;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeEmUsoException;
import com.github.heliommsfilho.foodapi.domain.exception.EstadoNaoEncontradoException;
import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Estado;
import com.github.heliommsfilho.foodapi.domain.repository.CidadeRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroCidadeService;
import com.github.heliommsfilho.foodapi.domain.service.CadastroEstadoService;
import com.github.heliommsfilho.foodapi.model.CidadeModel;
import com.github.heliommsfilho.foodapi.model.input.CidadeInput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/cidades")
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CadastroCidadeService cadastroCidadeService;
    private final CadastroEstadoService cadastroEstadoService;
    private final CidadeModelAssembler cidadeModelAssembler;
    private final CidadeInputDisassembler cidadeInputDisassembler;
    
    @Autowired
    public CidadeController(CidadeRepository cidadeRepository, CadastroCidadeService cadastroCidadeService, CadastroEstadoService cadastroEstadoService, CidadeModelAssembler cidadeModelAssembler, CidadeInputDisassembler cidadeInputDisassembler) {
        this.cidadeRepository = cidadeRepository;
        this.cadastroCidadeService = cadastroCidadeService;
        this.cadastroEstadoService = cadastroEstadoService;
        this.cidadeModelAssembler = cidadeModelAssembler;
        this.cidadeInputDisassembler = cidadeInputDisassembler;
    }
    
    @GetMapping
    public List<CidadeModel> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();
        
        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }
    
    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
        
        return cidadeModelAssembler.toModel(cidade);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
            
            cidade = cadastroCidadeService.salvar(cidade);
            
            return cidadeModelAssembler.toModel(cidade);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    @PutMapping("/{cidadeId}")
    public CidadeModel atualizar(@PathVariable Long cidadeId,
                                 @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
            
            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
            
            cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
            
            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cidade> remover(@PathVariable Long id) {
        try {
            cadastroCidadeService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EstadoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
