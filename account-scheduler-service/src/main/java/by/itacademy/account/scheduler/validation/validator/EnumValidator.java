package by.itacademy.account.scheduler.validation.validator;


import by.itacademy.account.scheduler.validation.anno.ValidType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidType, Enum<?>> {

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.name().equals("INVALID");
    }
}
