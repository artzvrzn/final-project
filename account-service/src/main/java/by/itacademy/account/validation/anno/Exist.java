package by.itacademy.account.validation.anno;

import by.itacademy.account.validation.validator.ClassifierListValidator;
import by.itacademy.account.validation.validator.ClassifierValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ClassifierValidator.class, ClassifierListValidator.class})
public @interface Exist {
    Classifier value();
    String message() default "doesn't exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    enum Classifier {

        CATEGORY("/operation/category/"),
        CURRENCY("/currency/");

        private final String url;

        Classifier(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
