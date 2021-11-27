package com.github.heliommsfilho.foodapi.controller;

import com.github.heliommsfilho.foodapi.assembler.PedidoInputDisassembler;
import com.github.heliommsfilho.foodapi.assembler.PedidoModelAssembler;
import com.github.heliommsfilho.foodapi.assembler.PedidoResumoModelAssembler;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import com.github.heliommsfilho.foodapi.domain.model.Pedido;
import com.github.heliommsfilho.foodapi.domain.model.Usuario;
import com.github.heliommsfilho.foodapi.domain.repository.PedidoRepository;
import com.github.heliommsfilho.foodapi.domain.repository.filter.PedidoFilter;
import com.github.heliommsfilho.foodapi.domain.service.EmissaoPedidoService;
import com.github.heliommsfilho.foodapi.infrastructure.repository.PedidoSpecs;
import com.github.heliommsfilho.foodapi.model.PedidoModel;
import com.github.heliommsfilho.foodapi.model.PedidoResumoModel;
import com.github.heliommsfilho.foodapi.model.input.PedidoInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;
    
    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    
    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;
    
    @GetMapping
    public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) {
        List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
        
        return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
    }
    
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
            
            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);
            
            novoPedido = emissaoPedido.emitir(novoPedido);
            
            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
}
