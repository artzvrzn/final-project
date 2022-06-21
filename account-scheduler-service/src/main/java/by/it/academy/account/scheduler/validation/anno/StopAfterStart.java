package by.it.academy.account.scheduler.validation.anno;

import by.it.academy.account.scheduler.validation.validator.StopAfterStartValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StopAfterStartValidator.class)
public @interface StopAfterStart {
    String message() default "startTime equal or greater than stopTime";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
