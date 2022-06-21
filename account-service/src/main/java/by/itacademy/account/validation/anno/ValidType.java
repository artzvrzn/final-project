package by.itacademy.account.validation.anno;

import by.itacademy.account.validation.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidType {
    String message() default "invalid type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


