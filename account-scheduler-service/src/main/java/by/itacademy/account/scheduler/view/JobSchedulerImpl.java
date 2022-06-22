package by.itacademy.account.scheduler.view;

import by.itacademy.account.scheduler.model.Schedule;
import by.itacademy.account.scheduler.model.ScheduledOperation;
import by.itacademy.account.scheduler.view.api.JobScheduler;
import by.itacademy.account.scheduler.view.api.UserService;
import by.itacademy.account.scheduler.view.jobs.ScheduledOperationJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Service
@Log4j2
public class JobSchedulerImpl implements JobScheduler {

    private static final String GROUP_NAME = "operations";
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;

    @Override
    public void schedule(ScheduledOperation scheduledOperation) {
        JobDetail jobDetail = getJobDetail(scheduledOperation);
        Trigger trigger = getTrigger(jobDetail, scheduledOperation.getSchedule());
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Operation {} has been scheduled", scheduledOperation.getId());
        } catch (SchedulerException exc) {
            String message = "failure during scheduling the job: ";
            log.error(message + exc.getMessage());
            throw new IllegalStateException(message, exc);
        }
    }

    @Override
    public void update(ScheduledOperation scheduledOperation) {
        UUID id = scheduledOperation.getId();
        try {
            scheduler.unscheduleJob(new TriggerKey(id.toString(), GROUP_NAME));
            scheduler.deleteJob(new JobKey(id.toString(), GROUP_NAME));
            JobDetail jobDetail = getJobDetail(scheduledOperation);
            Trigger trigger = getTrigger(jobDetail, scheduledOperation.getSchedule());
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Operation {} has been updated\n{}",
                    scheduledOperation.getId(), getTriggerInfo(trigger));
        } catch (SchedulerException exc) {
            String message = "failure during updating the job: ";
            log.error(message + exc.getMessage());
            throw new IllegalStateException(message, exc);
        }
    }

    @Override
    public void delete(ScheduledOperation scheduledOperation) {
        UUID id = scheduledOperation.getId();
        try {
            scheduler.unscheduleJob(new TriggerKey(id.toString(), GROUP_NAME));
            scheduler.deleteJob(new JobKey(id.toString(), GROUP_NAME));
        } catch (SchedulerException exc) {
            String message = "failure during deleting the job: ";
            log.error(exc.getMessage());
            throw new IllegalStateException(message, exc);
        }
    }

    @Override
    public void pause(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
            log.info("Job {} has been paused", jobKey);
        } catch (SchedulerException exc) {
            String message = "failed to pause the job: ";
            log.error(message + exc.getMessage());
            throw new IllegalStateException(message, exc.getCause());
        }
    }

    private JobDetail getJobDetail(ScheduledOperation scOperation) {
        UserDetails userDetails = userService.getUserDetails();
        UUID id = scOperation.getId();
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("operation", scOperation.getOperation());
        dataMap.put("user", userDetails);
        return JobBuilder.newJob(ScheduledOperationJob.class)
                .storeDurably()
                .withIdentity(id.toString(), GROUP_NAME)
                .usingJobData(dataMap)
                .build();
    }

    private Trigger getTrigger(JobDetail jobDetail, Schedule schedule) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), GROUP_NAME)
                .withSchedule(getScheduleBuilder(schedule))
                .startAt(conversionService.convert(schedule.getStartTime(), Date.class))
                .endAt(conversionService.convert(schedule.getStopTime(), Date.class))
                .build();
    }

    private CalendarIntervalScheduleBuilder getScheduleBuilder(Schedule schedule) {
        CalendarIntervalScheduleBuilder csb = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                .withMisfireHandlingInstructionFireAndProceed()
                .inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC.getId()));
        switch (schedule.getTimeUnit()) {
            case SECOND:
                return csb.withIntervalInSeconds(schedule.getInterval());
            case MINUTE:
                return csb.withIntervalInMinutes(schedule.getInterval());
            case HOUR:
                return csb.withIntervalInHours(schedule.getInterval());
            case DAY:
                return csb.withIntervalInDays(schedule.getInterval());
            case WEEK:
                return csb.withIntervalInWeeks(schedule.getInterval());
            case MONTH:
                return csb.withIntervalInMonths(schedule.getInterval());
            case YEAR:
                return csb.withIntervalInYears(schedule.getInterval());
            default:
                String message = "failed to convert TimeUnit to scheduleBuilder";
                log.error(message);
                throw new IllegalStateException(message);
        }
    }

    private String getTriggerInfo(Trigger trigger) {
        return String.format("%s, start at: %s, end at: %s",
                trigger.getKey(),
                trigger.getStartTime(),
                trigger.getEndTime());
    }
}
