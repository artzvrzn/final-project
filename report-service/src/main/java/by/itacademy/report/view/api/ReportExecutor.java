package by.itacademy.report.view.api;

import by.itacademy.report.model.ReportType;

import java.util.Map;
import java.util.UUID;

public interface ReportExecutor {

    void execute(UUID id, ReportType reportType, Map<String, Object> params);
}
