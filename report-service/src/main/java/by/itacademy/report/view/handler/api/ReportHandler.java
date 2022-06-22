package by.itacademy.report.view.handler.api;

import by.itacademy.report.model.FileData;

import java.util.Map;
import java.util.UUID;

public interface ReportHandler {

    FileData generate(UUID id, Map<String, Object> params);
}
