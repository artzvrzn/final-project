package by.itacademy.mail.scheduler.view;

import by.itacademy.mail.scheduler.dao.api.ScheduledMailRepository;
import by.itacademy.mail.scheduler.dao.entity.ScheduledMailReportEntity;
import by.itacademy.mail.scheduler.exception.RecordNotFoundException;
import by.itacademy.mail.scheduler.model.Schedule;
import by.itacademy.mail.scheduler.model.ScheduledMail;
import by.itacademy.mail.scheduler.view.api.JobScheduler;
import by.itacademy.mail.scheduler.view.api.MailSchedulerService;
import by.itacademy.mail.scheduler.view.api.UserService;
import by.itacademy.mail.scheduler.view.api.ValidationService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportMailSchedulerService implements MailSchedulerService {


    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ScheduledMailRepository repository;
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidationService reportValidationService;

    @Override
    public void create(ScheduledMail scheduledMail) {
        reportValidationService.validate(scheduledMail);
        LocalDateTime current = LocalDateTime.now(ZoneOffset.UTC);
        scheduledMail.setId(UUID.randomUUID());
        scheduledMail.setCreated(current);
        scheduledMail.setUpdated(current);
        updateDateIfZero(scheduledMail);
        repository.save(conversionService.convert(scheduledMail, ScheduledMailReportEntity.class));
        log.info("ScheduledMail {} has been created", scheduledMail.getId());
        jobScheduler.schedule(scheduledMail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScheduledMail> get(int page, int size) {
        UserDetails user = userService.getUserDetails();
        Pageable request = PageRequest.of(page, size, Sort.by("created").descending());
        if (userService.isAdmin(user)) {
            return repository.findAll(request).map(e -> conversionService.convert(e, ScheduledMail.class));
        } else {
            return repository.findAllByUsername(user.getUsername(), request)
                    .map(e -> conversionService.convert(e, ScheduledMail.class));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduledMail get(UUID id) {
        return conversionService.convert(getOrThrow(id), ScheduledMail.class);
    }

    @Override
    public void update(UUID id, LocalDateTime updated, ScheduledMail scheduledMail) {
        reportValidationService.validate(scheduledMail);
        ScheduledMailReportEntity entity = getOrThrow(id);
        checkUpdated(entity, updated);
        scheduledMail.setId(entity.getId());
        scheduledMail.setCreated(entity.getCreated());
        scheduledMail.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        updateDateIfZero(scheduledMail);
        repository.save(conversionService.convert(scheduledMail, ScheduledMailReportEntity.class));
        log.info("ScheduledMail {} has been updated", scheduledMail.getId());
        jobScheduler.update(scheduledMail);
    }

    @Override
    public void delete(UUID id, LocalDateTime updated) {
        ScheduledMailReportEntity entity = getOrThrow(id);
        checkUpdated(entity, updated);
        repository.delete(entity);
        log.info("ScheduledMail {} has been deleted", id);
        jobScheduler.delete(conversionService.convert(entity, ScheduledMail.class));
    }

    private ScheduledMailReportEntity getOrThrow(UUID id) {
        UserDetails userDetails = userService.getUserDetails();
        Optional<ScheduledMailReportEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException("scheduled mail doesn't exist");
        }
        ScheduledMailReportEntity entity = optional.get();
        if (!entity.getUsername().equals(userDetails.getUsername())) {
            if (!userService.isAdmin(userDetails)) {
                throw new AccessDeniedException("scheduled mail belongs to another user");
            }
        }
        return entity;
    }

    private void checkUpdated(ScheduledMailReportEntity entity, LocalDateTime updated) {
        if (!entity.getUpdated().equals(updated)) {
            throw new IllegalArgumentException("scheduled mail has already been updated");
        }
    }

    private void updateDateIfZero(ScheduledMail scheduledMail) {
        final int delay = 30;
        LocalDateTime epochZero = LocalDate.EPOCH.atStartOfDay();
        boolean isStartUpdated = false;
        Schedule schedule = scheduledMail.getSchedule();
        if (schedule.getStartTime().isEqual(epochZero)) {
            LocalDateTime newStartTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(delay);
            log.info("ScheduledMail {}: setting startTime value from 0 to {}",
                    scheduledMail.getId(),
                    newStartTime);
            schedule.setStartTime(newStartTime);
            isStartUpdated = true;
        }
        if (isStartUpdated) {
            epochZero = epochZero.plusSeconds(delay);
            schedule.setStopTime(schedule.getStopTime().plusSeconds(delay));
        }
        if (schedule.getStopTime().isEqual(epochZero)) {
            LocalDateTime newStopTime = LocalDate.ofYearDay(2100, 1).atStartOfDay();
            log.info("ScheduledMail {}: setting stopTime value from 0 to {}",
                    scheduledMail.getId(),
                    newStopTime);
            schedule.setStopTime(newStopTime);
        }
    }
}
