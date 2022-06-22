package by.itacademy.account.scheduler.view.api;

import by.itacademy.account.scheduler.model.ScheduledOperation;
import org.quartz.JobKey;

public interface JobScheduler {

    void schedule(ScheduledOperation scheduledOperation);

    void update(ScheduledOperation scheduledOperation);

    void delete(ScheduledOperation scheduledOperation);

    void pause(JobKey jobKey);
}
