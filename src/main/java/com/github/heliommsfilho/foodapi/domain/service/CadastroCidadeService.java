package com.github.heliommsfilho.foodapi.domain.service;

import com.github.heliommsfilho.foodapi.domain.exception.CidadeNaoEncontradaException;
import com.github.heliommsfilho.foodapi.domain.exception.EntidadeEmUsoException;
import com.github.heliommsfilho.foodapi.domain.model.Cidade;
import com.github.heliommsfilho.foodapi.domain.model.Estado;
import com.github.heliommsfilho.foodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCidadeService {

    public static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

    private final CidadeRepository cidadeRepository;
    private final CadastroEstadoService cadastroEstadoService;

    @Autowired
    public CadastroCidadeService(CidadeRepository cidadeRepository, CadastroEstadoService cadastroEstadoService) {
        this.cidadeRepository = cidadeRepository;
        this.cadastroEstadoService = cadastroEstadoService;
    }

    @Transactional
    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);
        cidade.setEstado(estado);

        return cidadeRepository.save(cidade);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            cidadeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
        } catch (EmptyResultDataAccessException e) {
            throw new CidadeNaoEncontradaException(id);
        }
    }

    public Cidade buscarOuFalhar(Long id) {
        return cidadeRepository.findById(id).orElseThrow(() -> new CidadeNaoEncontradaException(id));
    }
}
