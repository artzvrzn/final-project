package by.it.academy.report.view;

import by.it.academy.report.dao.api.ReportFilePropertyRepository;
import by.it.academy.report.dao.api.ReportRepository;
import by.it.academy.report.dao.entity.ReportEntity;
import by.it.academy.report.dao.entity.ReportFilePropertyEntity;
import by.it.academy.report.model.FileData;
import by.it.academy.report.model.ReportStatus;
import by.it.academy.report.utils.JwtTokenUtil;
import by.it.academy.report.view.api.StorageService;
import by.it.academy.report.view.api.ReportExecutor;
import by.it.academy.report.view.handler.ReportHandlerFactory;
import by.it.academy.report.view.handler.api.ReportHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ExecutorService executorService;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportFilePropertyRepository filePropertyRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ReportHandlerFactory handlerFactory;

    @Override
    public void execute(UUID id) {
        ReportEntity reportEntity = reportRepository.getById(id);
        reportRepository.updateStatus(id, ReportStatus.PROGRESS, LocalDateTime.now(ZoneOffset.UTC));
        executorService.submit(() -> {
            try {
                ReportHandler reportHandler = handlerFactory.getHandler(reportEntity.getType());
                FileData fileData = reportHandler.generate(id, reportEntity.getParams());
                filePropertyRepository.updateFilename(id, fileData.getFilename());
                storageService.upload(fileData);
                reportRepository.updateStatus(id, ReportStatus.DONE, LocalDateTime.now(ZoneOffset.UTC));
                log.info("Report generation completed: {}", id);
            } catch (Exception e) {
                log.error("Failure during generating of a report: {}", e.getMessage(), e);
                reportRepository.updateStatus(id, ReportStatus.ERROR, LocalDateTime.now(ZoneOffset.UTC));
            }
        });
    }
}
