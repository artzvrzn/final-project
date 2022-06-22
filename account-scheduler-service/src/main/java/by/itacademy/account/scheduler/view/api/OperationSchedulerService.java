package by.itacademy.account.scheduler.view.api;

import by.itacademy.account.scheduler.model.ScheduledOperation;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationSchedulerService {

    void create(ScheduledOperation dto);

    Page<ScheduledOperation> get(int page, int size);

    ScheduledOperation get(UUID id);

    void update(UUID id, LocalDateTime updated, ScheduledOperation dto);

    void delete(UUID id, LocalDateTime updated);
}
