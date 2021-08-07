package com.github.heliommsfilho.foodapi.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Objects;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {

    private int numeroMultiplo;

    @Override
    public void initialize(Multiplo constraintAnnotation) {
        numeroMultiplo = constraintAnnotation.numero();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (Objects.nonNull(value)) {
            final BigDecimal decimalValue  = BigDecimal.valueOf(value.doubleValue());
            final BigDecimal multiploValue = BigDecimal.valueOf(this.numeroMultiplo);

            isValid = decimalValue.remainder(multiploValue).compareTo(BigDecimal.ZERO) == 0;
        }

        return isValid;
    }
}
