package com.github.heliommsfilho.foodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException {

    public NegocioException(String message) {
        super(message);
    }


    public NegocioException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
