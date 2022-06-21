package by.it.academy.account.scheduler.validation.anno;

import by.it.academy.account.scheduler.validation.validator.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeValidator.class)
public @interface FutureTime {
    String message() default "has to be in future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
