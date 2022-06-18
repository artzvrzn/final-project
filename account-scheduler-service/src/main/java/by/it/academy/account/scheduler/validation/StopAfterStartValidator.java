package by.it.academy.account.scheduler.validation;

import by.it.academy.account.scheduler.model.Schedule;
import by.it.academy.account.scheduler.validation.anno.StopAfterStart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StopAfterStartValidator implements ConstraintValidator<StopAfterStart, Schedule> {

    @Override
    public boolean isValid(Schedule schedule, ConstraintValidatorContext context) {
        if (schedule == null) {
            return true;
        }
        LocalDateTime start = schedule.getStartTime();
        LocalDateTime stop = schedule.getStopTime();
        LocalDateTime epochZero = LocalDate.EPOCH.atStartOfDay();
        if (start.isEqual(epochZero) && stop.isEqual(epochZero)) {
            return true;
        }
        return start.isEqual(stop) ? false : stop.isAfter(start);
    }
}
