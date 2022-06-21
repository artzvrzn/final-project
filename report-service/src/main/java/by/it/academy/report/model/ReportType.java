package by.it.academy.report.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum ReportType {

    BALANCE("Отчет по балансу аккаунтов"),
    BY_DATE("Отчет по операциям, осуществленным в разрезе дат"),
    BY_CATEGORY("Отчет по операциям, совершенным в разрезе категорий");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
