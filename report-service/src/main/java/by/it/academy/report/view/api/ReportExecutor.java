package by.it.academy.report.view.api;

import by.it.academy.report.model.Report;
import by.it.academy.report.model.ReportType;

import java.util.Map;
import java.util.UUID;

public interface ReportExecutor {

    void execute(UUID id, ReportType reportType, Map<String, Object> params);
}
