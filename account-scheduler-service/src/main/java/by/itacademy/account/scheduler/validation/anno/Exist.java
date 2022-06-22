package by.itacademy.account.scheduler.validation.anno;

import by.itacademy.account.scheduler.validation.validator.UUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UUIDValidator.class})
public @interface Exist {
    ServiceType value();
    String message() default "doesn't exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    enum ServiceType {
        ACCOUNT, CATEGORY, CURRENCY
    }
}
