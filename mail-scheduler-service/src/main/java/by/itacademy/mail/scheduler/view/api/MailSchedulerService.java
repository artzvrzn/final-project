package by.itacademy.mail.scheduler.view.api;

import by.itacademy.mail.scheduler.model.ScheduledMail;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MailSchedulerService {

    void create(ScheduledMail dto);

    Page<ScheduledMail> get(int page, int size);

    ScheduledMail get(UUID id);

    void update(UUID id, LocalDateTime updated, ScheduledMail dto);

    void delete(UUID id, LocalDateTime updated);
}
