package by.itacademy.report.view;

import by.itacademy.report.dao.api.ReportFilePropertyRepository;
import by.itacademy.report.dao.api.ReportRepository;
import by.itacademy.report.model.FileData;
import by.itacademy.report.model.ReportStatus;
import by.itacademy.report.model.ReportType;
import by.itacademy.report.view.api.StorageService;
import by.itacademy.report.view.api.ReportExecutor;
import by.itacademy.report.view.handler.ReportHandlerFactory;
import by.itacademy.report.view.handler.api.ReportHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@Log4j2
public class ReportExecutorImpl implements ReportExecutor {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportFilePropertyRepository filePropertyRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ReportHandlerFactory handlerFactory;
    @Autowired
    private ConversionService conversionService;

    @Override
    @Async("SecurityAwareTaskExecutor")
    public void execute(UUID id, ReportType reportType, Map<String, Object> params) {
        log.debug("Updating status: {}", ReportStatus.PROGRESS);
        reportRepository.updateStatus(id, ReportStatus.PROGRESS, LocalDateTime.now(ZoneOffset.UTC));
        try {
            ReportHandler reportHandler = handlerFactory.getHandler(reportType);
            log.debug("Generating report: {}", id);
            FileData fileData = reportHandler.generate(id, params);
            log.debug("Updating filename: {}", id);
            filePropertyRepository.updateFilename(id, fileData.getFilename());
            log.debug("Uploading file: {}", id);
            storageService.upload(fileData);
            log.debug("Updating status: {}", ReportStatus.DONE);
            reportRepository.updateStatus(id, ReportStatus.DONE, LocalDateTime.now(ZoneOffset.UTC));
            log.info("Report generation completed: {}", id);
        } catch (Exception e) {
            log.error("Failure during generating of a report: {}", e.getMessage(), e);
            reportRepository.updateStatus(id, ReportStatus.ERROR, LocalDateTime.now(ZoneOffset.UTC));
        }
    }
}
