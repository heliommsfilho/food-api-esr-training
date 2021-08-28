package com.github.heliommsfilho.foodapi.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.io.Serial;

@AllArgsConstructor
public class ValidacaoException extends RuntimeException {
    
    @Serial
    private static final long serialVersionUID = 2900708171284678797L;
    
    @Getter
    private final BindingResult bindingResult;
}
