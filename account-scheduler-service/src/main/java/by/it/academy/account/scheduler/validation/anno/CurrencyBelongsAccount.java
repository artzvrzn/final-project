package by.it.academy.account.scheduler.validation.anno;

import by.it.academy.account.scheduler.validation.validator.CurrencyAccountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyAccountValidator.class)
public @interface CurrencyBelongsAccount {

    String message() default "invalid currency for account";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
