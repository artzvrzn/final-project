package by.itacademy.account.scheduler.view;

import by.itacademy.account.scheduler.dao.api.ScheduledOperationRepository;
import by.itacademy.account.scheduler.dao.entity.ScheduledOperationEntity;
import by.itacademy.account.scheduler.exception.RecordNotFoundException;
import by.itacademy.account.scheduler.model.Schedule;
import by.itacademy.account.scheduler.model.ScheduledOperation;
import by.itacademy.account.scheduler.view.api.JobScheduler;
import by.itacademy.account.scheduler.view.api.OperationSchedulerService;
import by.itacademy.account.scheduler.view.api.UserService;
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

@Service
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class OperationSchedulerServiceImpl implements OperationSchedulerService {

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ScheduledOperationRepository repository;
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private UserService userService;

    @Override
    public void create(ScheduledOperation scheduledOperation) {
        LocalDateTime current = LocalDateTime.now(ZoneOffset.UTC);
        scheduledOperation.setId(UUID.randomUUID());
        scheduledOperation.setCreated(current);
        scheduledOperation.setUpdated(current);
        updateDateIfZero(scheduledOperation);
        repository.save(conversionService.convert(scheduledOperation, ScheduledOperationEntity.class));
        log.info("ScheduledOperation {} has been created", scheduledOperation.getId());
        jobScheduler.schedule(scheduledOperation);
    }

    ////
    @Override
    @Transactional(readOnly = true)
    public Page<ScheduledOperation> get(int page, int size) {
        UserDetails user = userService.getUserDetails();
        Pageable request = PageRequest.of(page, size, Sort.by("created").descending());
        if (userService.isAdmin(user)) {
            return repository.findAll(request).map(e -> conversionService.convert(e, ScheduledOperation.class));
        } else {
            return repository.findAllByUsername(
                    user.getUsername(), request).map(e -> conversionService.convert(e, ScheduledOperation.class));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduledOperation get(UUID id) {
        return conversionService.convert(getOrThrow(id), ScheduledOperation.class);
    }

    @Override
    public void update(UUID id, LocalDateTime updated, ScheduledOperation dto) {
        ScheduledOperationEntity entity = getOrThrow(id);
        checkUpdated(entity, updated);
        dto.setId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        updateDateIfZero(dto);
        repository.save(conversionService.convert(dto, ScheduledOperationEntity.class));
        log.info("ScheduledOperation {} has been updated", dto.getId());
        jobScheduler.update(dto);
    }

    @Override
    public void delete(UUID id, LocalDateTime updated) {
        ScheduledOperationEntity entity = getOrThrow(id);
        checkUpdated(entity, updated);
        repository.delete(entity);
        log.info("ScheduledOperation {} has been deleted", id);
        jobScheduler.delete(conversionService.convert(entity, ScheduledOperation.class));
    }

    private ScheduledOperationEntity getOrThrow(UUID id) {
        UserDetails userDetails = userService.getUserDetails();
        Optional<ScheduledOperationEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException("scheduled operation doesn't exist");
        }
        ScheduledOperationEntity entity = optional.get();
        if (!entity.getUsername().equals(userDetails.getUsername())) {
            if (!userService.isAdmin(userDetails)) {
                throw new AccessDeniedException("scheduled operation belongs to another user");
            }
        }
        return entity;
    }

    private void checkUpdated(ScheduledOperationEntity entity, LocalDateTime updated) {
        if (!entity.getUpdated().equals(updated)) {
            throw new IllegalArgumentException("scheduled operation has already been updated");
        }
    }

    private void updateDateIfZero(ScheduledOperation scheduledOperation) {
        final int delay = 30;
        LocalDateTime epochZero = LocalDate.EPOCH.atStartOfDay();
        boolean isStartUpdated = false;
        Schedule schedule = scheduledOperation.getSchedule();
        if (schedule.getStartTime().isEqual(epochZero)) {
            LocalDateTime newStartTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(delay);
            log.info("ScheduledOperation {}: setting startTime value from 0 to {}",
                    scheduledOperation.getId(),
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
            log.info("ScheduledOperation {}: setting stopTime value from 0 to {}",
                    scheduledOperation.getId(),
                    newStopTime);
            schedule.setStopTime(newStopTime);
        }
    }
}
