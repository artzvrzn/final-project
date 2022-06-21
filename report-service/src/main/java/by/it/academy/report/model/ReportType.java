package by.it.academy.report.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum ReportType {

    BALANCE, BY_DATE, BY_CATEGORY;

    @JsonCreator
    public static ReportType valueOfOrNull(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return ReportType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exc) {
            log.error(exc.getMessage());
            return null;
        }
    }
}
