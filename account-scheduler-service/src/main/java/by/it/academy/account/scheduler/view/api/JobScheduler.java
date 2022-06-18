package by.it.academy.account.scheduler.view.api;

import by.it.academy.account.scheduler.model.ScheduledOperation;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

public interface JobScheduler {

    void schedule(ScheduledOperation scheduledOperation);

    void update(ScheduledOperation scheduledOperation);

    void delete(ScheduledOperation scheduledOperation);

    void pause(JobKey jobKey);
}
