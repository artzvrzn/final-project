package by.itacademy.mail.scheduler.view;

import by.itacademy.mail.scheduler.model.Schedule;
import by.itacademy.mail.scheduler.model.ScheduledMail;
import by.itacademy.mail.scheduler.view.api.JobScheduler;
import by.itacademy.mail.scheduler.view.api.UserService;
import by.itacademy.mail.scheduler.view.job.MailReportJob;
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

@Log4j2
@Service
public class ReportMailJobScheduler implements JobScheduler {

    private static final String GROUP_NAME = "reports";

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;

    @Override
    public void schedule(ScheduledMail scheduledMail) {
        JobDetail jobDetail = getJobDetail(scheduledMail);
        Trigger trigger = getTrigger(jobDetail, scheduledMail);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job {} has been scheduled", scheduledMail.getId());
        } catch (SchedulerException exc) {
            String message = "failure during scheduling the job: ";
            log.error(message + exc.getMessage());
            throw new IllegalStateException(message, exc);
        }
    }

    @Override
    public void update(ScheduledMail scheduledMail) {
        UUID id = scheduledMail.getId();
        try {
            scheduler.unscheduleJob(new TriggerKey(id.toString(), GROUP_NAME));
            scheduler.deleteJob(new JobKey(id.toString(), GROUP_NAME));
            JobDetail jobDetail = getJobDetail(scheduledMail);
            Trigger trigger = getTrigger(jobDetail, scheduledMail);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job {} has been updated. {}",
                    scheduledMail.getId(), getTriggerInfo(trigger));
        } catch (SchedulerException exc) {
            String message = "failure during updating the job: ";
            log.error(message + exc.getMessage());
            throw new IllegalStateException(message, exc);
        }
    }

    @Override
    public void delete(ScheduledMail scheduledMail) {
        UUID id = scheduledMail.getId();
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

    private JobDetail getJobDetail(ScheduledMail scheduledMail) {
        UserDetails userDetails = userService.getUserDetails();
        UUID id = scheduledMail.getId();
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("mail", scheduledMail.getMail());
        dataMap.put("schedule", scheduledMail.getSchedule());
        dataMap.put("user", userDetails);
        return JobBuilder.newJob(MailReportJob.class)
                .storeDurably()
                .withIdentity(id.toString(), GROUP_NAME)
                .usingJobData(dataMap)
                .build();
    }

    private Trigger getTrigger(JobDetail jobDetail, ScheduledMail scheduledMail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), GROUP_NAME)
                .withSchedule(getScheduleBuilder(scheduledMail.getSchedule()))
                .startAt(conversionService.convert(scheduledMail.getSchedule().getStartTime(), Date.class))
                .endAt(conversionService.convert(scheduledMail.getSchedule().getStopTime(), Date.class))
                .build();
    }


    private CalendarIntervalScheduleBuilder getScheduleBuilder(Schedule schedule) {
        CalendarIntervalScheduleBuilder csb = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                .withMisfireHandlingInstructionFireAndProceed()
                .inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC.getId()));
        switch (schedule.getTimeUnit()) {
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
