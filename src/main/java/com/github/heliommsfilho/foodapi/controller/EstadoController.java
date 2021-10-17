package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.EstadoInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.EstadoModelAssembler;
import com.github.heliommsfilho.foodapi.domain.model.Estado;
import com.github.heliommsfilho.foodapi.domain.repository.EstadoRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroEstadoService;
import com.github.heliommsfilho.foodapi.model.EstadoModel;
import com.github.heliommsfilho.foodapi.model.input.EstadoInput;
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
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoRepository estadoRepository;
    private final CadastroEstadoService cadastroEstadoService;
    private final EstadoModelAssembler estadoModelAssembler;
    private final EstadoInputDisassembler estadoInputDisassembler;
    
    @Autowired
    public EstadoController(EstadoRepository estadoRepository, CadastroEstadoService cadastroEstadoService, EstadoModelAssembler estadoModelAssembler, EstadoInputDisassembler estadoInputDisassembler) {
        this.estadoRepository = estadoRepository;
        this.cadastroEstadoService = cadastroEstadoService;
        this.estadoModelAssembler = estadoModelAssembler;
        this.estadoInputDisassembler = estadoInputDisassembler;
    }
    
    @GetMapping
    public List<EstadoModel> listar() {
        List<Estado> todosEstados = estadoRepository.findAll();
        
        return estadoModelAssembler.toCollectionModel(todosEstados);
    }
    
    @GetMapping("/{estadoId}")
    public EstadoModel buscar(@PathVariable Long estadoId) {
        Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);
        
        return estadoModelAssembler.toModel(estado);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
        
        estado = cadastroEstadoService.salvar(estado);
        
        return estadoModelAssembler.toModel(estado);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cadastroEstadoService.excluir(id);
    }
    
    @PutMapping("/{estadoId}")
    public EstadoModel atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);
        
        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
        
        estadoAtual = cadastroEstadoService.salvar(estadoAtual);
        
        return estadoModelAssembler.toModel(estadoAtual);
    }
}
