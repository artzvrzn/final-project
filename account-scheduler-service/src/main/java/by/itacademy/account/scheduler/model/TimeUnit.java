package by.itacademy.account.scheduler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

@Log4j2
public enum TimeUnit implements Serializable {

    SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR, INVALID;

    @JsonCreator
    public static TimeUnit valueOfOrInvalid(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return TimeUnit.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return INVALID;
        }
    }
}
