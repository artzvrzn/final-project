package by.itacademy.report.view.api;

import by.itacademy.report.model.ReportType;

import java.util.Map;

public interface ValidationService {

    void validate(ReportType type, Map<String, Object> params);
}
