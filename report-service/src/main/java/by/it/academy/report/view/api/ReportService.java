package by.it.academy.report.view.api;

import by.it.academy.report.model.FileData;
import by.it.academy.report.model.Report;
import by.it.academy.report.model.ReportType;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public interface ReportService {

    void create(ReportType type, Map<String, Object> params);

    Page<Report> get(int page, int size);

    Report get(UUID id);

    FileData getFile(UUID id);

    boolean isReady(UUID id);
}
