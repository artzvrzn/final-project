package by.it.academy.account.scheduler.validation.validator;

import by.it.academy.account.scheduler.validation.anno.FutureTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeValidator implements ConstraintValidator<FutureTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime ldt, ConstraintValidatorContext context) {
        if (ldt == null) {
            return true;
        }
        if (ldt.isEqual(LocalDate.EPOCH.atStartOfDay())) {
            return true;
        }
        return ldt.isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }
}
