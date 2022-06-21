package by.it.academy.report.view;

import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.dao.api.ReportRepository;
import by.it.academy.report.dao.entity.ReportEntity;
import by.it.academy.report.exception.RecordNotFoundException;
import by.it.academy.report.exception.ValidationException;
import by.it.academy.report.model.*;
import by.it.academy.report.utils.MapParser;
import by.it.academy.report.validation.validator.ParamsValidatorFactory;
import by.it.academy.report.view.api.StorageService;
import by.it.academy.report.view.api.ReportExecutor;
import by.it.academy.report.view.api.ReportService;
import by.it.academy.report.view.api.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ParamsValidatorFactory validatorFactory;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportExecutor reportExecutor;
    @Autowired
    private MapParser parser;

    @Override
    public void create(ReportType type, Map<String, Object> params) {
        List<Violation> violations = validatorFactory.getValidator(type).validate(params);
        if (!violations.isEmpty()) {
            throw new ValidationException("Validation failed", violations);
        }
        Report report = createReport(type, params);
        reportRepository.save(conversionService.convert(report, ReportEntity.class));
        log.info("Report {} has been requested for creation", report.getId());
        reportExecutor.execute(report.getId(), report.getType(), report.getParams());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report> get(int page, int size) {
        UserDetails user = userService.getUserDetails();
        Pageable request = PageRequest.of(page, size, Sort.by("created").descending());
        Page<Report> result;
        if (userService.isAdmin(user)) {
            result = reportRepository.findAll(request).map(e -> conversionService.convert(e, Report.class));
        } else {
            result = reportRepository.findAllByUsername
                    (user.getUsername(), request).map(e -> conversionService.convert(e, Report.class));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Report get(UUID id) {
        return conversionService.convert(getOrThrow(id), Report.class);
    }

    @Override
    public FileData getFile(UUID id) {
        ReportEntity entity = getOrThrow(id);
        if (!entity.getStatus().equals(ReportStatus.DONE)) {
            throw new NoSuchElementException("Report file is not created");
        }
        String filename = entity.getFileProperty().getFilename();
        Future<FileData> futureFile = storageService.download(filename);
        try {
            return futureFile.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred during receiving result from storage service {}", filename, e.getCause());
            return null;
        }
    }

    @Override
    public boolean isReady(UUID id) {
        Optional<ReportEntity> optional = reportRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }
        return optional.get().getStatus().equals(ReportStatus.DONE);
    }

    private Report createReport(ReportType type, Map<String, Object> params) {
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        return new Report.Builder()
                .withCreated(currentTime)
                .withUpdated(currentTime)
                .withId(UUID.randomUUID())
                .withStatus(ReportStatus.LOADED)
                .withReportType(type)
                .withDescription(resolveDescription(type, params))
                .withParams(params)
                .build();
    }

    private String resolveDescription(ReportType type, Map<String, Object> params) {
        switch (type) {
            case BALANCE:
                List<UUID> accounts = parser.readList("accounts", params, UUID.class);
                return type.getDescription() + ": " +
                        accounts.stream().map(String::valueOf).collect(Collectors.joining(", "));
            case BY_DATE:
                LocalDate from = parser.readValue("from", params, LocalDate.class);
                LocalDate to = parser.readValue("to", params, LocalDate.class);
                return String.format("Отчет по операциям, осуществленным с %s по %s", from, to);
            case BY_CATEGORY:
                List<UUID> categories = parser.readList("categories", params, UUID.class);
                return type.getDescription() + ": " +
                        categories.stream().map(String::valueOf).collect(Collectors.joining(", "));
            default:
                return type.getDescription();
        }
    }

    private ReportEntity getOrThrow(UUID id) {
        UserDetails userDetails = userService.getUserDetails();
        Optional<ReportEntity> optional = reportRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException("Report doesn't exist");
        }
        ReportEntity entity = optional.get();
        if (!entity.getUsername().equals(userDetails.getUsername())) {
            if (!userService.isAdmin(userDetails)) {
                throw new AccessDeniedException("Report belongs to another user");
            }
        }
        return entity;
    }
}
