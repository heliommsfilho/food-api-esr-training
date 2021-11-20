package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.exception.NegocioException;
import com.github.heliommsfilho.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.model.FormaPagamento;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.model.Usuario;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroRestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    private final CadastroCozinhaService cadastroCozinhaService;
    private final CadastroCidadeService cadastroCidadeService;
    private final CadastroFormaPagamentoService cadastroFormaPagamentoService;
    private final CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    public CadastroRestauranteService(RestauranteRepository restauranteRepository,
                                      CadastroCozinhaService cadastroCozinhaService,
                                      CadastroCidadeService cadastroCidadeService,
                                      CadastroFormaPagamentoService cadastroFormaPagamentoService,
                                      CadastroUsuarioService cadastroUsuarioService) {
        this.restauranteRepository = restauranteRepository;
        this.cadastroCozinhaService = cadastroCozinhaService;
        this.cadastroCidadeService = cadastroCidadeService;
        this.cadastroFormaPagamentoService = cadastroFormaPagamentoService;
        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
        restaurante.setCozinha(cozinha);
        
        final Long id = restaurante.getEndereco().getCidade().getId();
        final Cidade cidade = cadastroCidadeService.buscarOuFalhar(id);
        restaurante.getEndereco().setCidade(cidade);

        return restauranteRepository.save(restaurante);
    }
    
    @Transactional
    public void ativar(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.ativar();
        
        restauranteRepository.save(restauranteAtual);
    }
    
    @Transactional
    public void inativar(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.inativar();
        
        restauranteRepository.save(restauranteAtual);
    }
    
    @Transactional
    public void ativar (final List<Long> restauranteIds) {
        try {
            restauranteIds.forEach(this::ativar);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    @Transactional
    public void inativar (final List<Long> restauranteIds) {
        try {
            restauranteIds.forEach(this::inativar);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    @Transactional
    public void desassociarFormaPagamento(final Long restauranteId, final Long formaPagamentoId) {
        final Restaurante restaurante = buscarOuFalhar(restauranteId);
        final FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
        
        restaurante.removerFormaPagamento(formaPagamento);
    }
    
    @Transactional
    public void associarFormaPagamento(final Long restauranteId, final Long formaPagamentoId) {
        final Restaurante restaurante = buscarOuFalhar(restauranteId);
        final FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
        
        restaurante.adicionarFormaPagamento(formaPagamento);
    }
    
    @Transactional
    public void abrir(Long restauranteId) {
        Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
        
        restauranteAtual.abrir();
    }
    
    @Transactional
    public void fechar(Long restauranteId) {
        Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
        
        restauranteAtual.fechar();
    }
    
    @Transactional
    public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        
        restaurante.removerResponsavel(usuario);
    }
    
    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        
        restaurante.adicionarResponsavel(usuario);
    }
    
    public Restaurante buscarOuFalhar(Long id) {
        return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }
}
