package by.itacademy.mail.scheduler.converter;

import by.itacademy.mail.scheduler.dao.entity.ScheduledMailReportEntity;
import by.itacademy.mail.scheduler.model.Mail;
import by.itacademy.mail.scheduler.model.Report;
import by.itacademy.mail.scheduler.model.Schedule;
import by.itacademy.mail.scheduler.model.ScheduledMail;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduledReportEntityToScheduledMailConverter
        implements Converter<ScheduledMailReportEntity, ScheduledMail> {
    @Override
    public ScheduledMail convert(ScheduledMailReportEntity source) {
        ScheduledMail dto = new ScheduledMail();
        dto.setId(source.getId());
        dto.setCreated(source.getCreated());
        dto.setUpdated(source.getUpdated());
        Schedule schedule = new Schedule();
        schedule.setStartTime(source.getStartTime());
        schedule.setStopTime(source.getStopTime());
        schedule.setInterval(source.getInterval());
        schedule.setTimeUnit(source.getTimeUnit());
        dto.setSchedule(schedule);
        Mail mail = new Mail();
        mail.setReceiver(source.getReceiver());
        Report report = new Report();
        report.setType(source.getType());
        report.setParams(source.getParams());
        mail.setSubject(report);
        dto.setMail(mail);
        return dto;
    }
}
