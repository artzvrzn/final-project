package by.itacademy.report.view.api;

import by.itacademy.report.model.FileData;
import by.itacademy.report.model.Report;
import by.itacademy.report.model.ReportType;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.UUID;

public interface ReportService {

    UUID create(ReportType type, Map<String, Object> params);

    Page<Report> get(int page, int size);

    Report get(UUID id);

    FileData getFile(UUID id);

    boolean isReady(UUID id);
}
