package com.github.heliommsfilho.foodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class EntidadeNaoEncontradaException extends NegocioException {

    EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}
