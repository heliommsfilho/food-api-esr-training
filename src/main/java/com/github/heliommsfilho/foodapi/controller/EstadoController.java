package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.domain.exception.EntidadeEmUsoException;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.model.Estado;
import com.github.heliommsfilho.foodapi.domain.repository.EstadoRepository;
import com.github.heliommsfilho.foodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoRepository estadoRepository;

    private final CadastroEstadoService cadastroEstadoService;

    @Autowired
    public EstadoController(EstadoRepository estadoRepository, CadastroEstadoService cadastroEstadoService) {
        this.estadoRepository = estadoRepository;
        this.cadastroEstadoService = cadastroEstadoService;
    }

    @GetMapping
    private List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}")
    private ResponseEntity<Estado> buscar(@PathVariable Long id) {
        Optional<Estado> estadoOptional = estadoRepository.findById(id);
        return estadoOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<Estado> salvar(@RequestBody Estado estado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoRepository.save(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Estado> remover(@PathVariable Long id) {
        try {
            cadastroEstadoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Estado estado) {
        Optional<Estado> estadoOptional = estadoRepository.findById(id);

        if (estadoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Estado estadoAtual = estadoOptional.get();
        BeanUtils.copyProperties(estado, estadoAtual, "id");

        try {
            Estado estadoAtualizado = cadastroEstadoService.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtualizado);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
