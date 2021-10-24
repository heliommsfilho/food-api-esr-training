package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Cozinha;
import com.github.heliommsfilho.foodapi.domain.model.Restaurante;
import com.github.heliommsfilho.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroRestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    private final CadastroCozinhaService cadastroCozinhaService;
    private final CadastroCidadeService cadastroCidadeService;

    @Autowired
    public CadastroRestauranteService(RestauranteRepository restauranteRepository,
                                      CadastroCozinhaService cadastroCozinhaService,
                                      CadastroCidadeService cadastroCidadeService) {
        this.restauranteRepository = restauranteRepository;
        this.cadastroCozinhaService = cadastroCozinhaService;
        this.cadastroCidadeService = cadastroCidadeService;
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

    public Restaurante buscarOuFalhar(Long id) {
        return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }
}
