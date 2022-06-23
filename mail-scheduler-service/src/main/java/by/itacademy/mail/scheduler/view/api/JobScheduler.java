package by.itacademy.mail.scheduler.view.api;

import by.itacademy.mail.scheduler.model.ScheduledMail;
import org.quartz.JobKey;

public interface JobScheduler {

    void schedule(ScheduledMail scheduledMail);

    void update(ScheduledMail scheduledMail);

    void delete(ScheduledMail scheduledMail);

    void pause(JobKey jobKey);
}
