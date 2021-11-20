package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.PermissaoModelAssembler;
import com.github.heliommsfilho.foodapi.domain.model.Grupo;
import com.github.heliommsfilho.foodapi.domain.service.CadastroGrupoService;
import com.github.heliommsfilho.foodapi.model.PermissaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {
    
    @Autowired
    private CadastroGrupoService cadastroGrupo;
    
    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;
    
    @GetMapping
    public List<PermissaoModel> listar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        
        return permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());
    }
    
    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
    }
    
    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.associarPermissao(grupoId, permissaoId);
    }
}