package by.it.academy.report.view.handler.api;

import by.it.academy.report.model.FileData;

import java.util.Map;
import java.util.UUID;

public interface ReportHandler {

    FileData generate(UUID id, Map<String, Object> params);
}
