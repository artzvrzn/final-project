package by.it.academy.report.view;

import by.it.academy.report.dao.api.ReportFilePropertyRepository;
import by.it.academy.report.dao.api.ReportRepository;
import by.it.academy.report.dao.entity.ReportEntity;
import by.it.academy.report.dao.entity.ReportFilePropertyEntity;
import by.it.academy.report.model.FileData;
import by.it.academy.report.model.Report;
import by.it.academy.report.model.ReportStatus;
import by.it.academy.report.model.ReportType;
import by.it.academy.report.utils.JwtTokenUtil;
import by.it.academy.report.view.api.StorageService;
import by.it.academy.report.view.api.ReportExecutor;
import by.it.academy.report.view.handler.ReportHandlerFactory;
import by.it.academy.report.view.handler.api.ReportHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

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
