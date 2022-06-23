package by.itacademy.mail.scheduler.converter;

import by.itacademy.mail.scheduler.dao.entity.ScheduledMailReportEntity;
import by.itacademy.mail.scheduler.model.Mail;
import by.itacademy.mail.scheduler.model.Report;
import by.itacademy.mail.scheduler.model.Schedule;
import by.itacademy.mail.scheduler.model.ScheduledMail;
import by.itacademy.mail.scheduler.view.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduledMailToScheduledMailReportEntityConverter
        implements Converter<ScheduledMail, ScheduledMailReportEntity> {

    @Autowired
    private UserService userService;

    @Override
    public ScheduledMailReportEntity convert(ScheduledMail dto) {
        ScheduledMailReportEntity entity = new ScheduledMailReportEntity();
        entity.setId(dto.getId());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        Schedule schedule = dto.getSchedule();
        entity.setStartTime(schedule.getStartTime());
        entity.setStopTime(schedule.getStopTime());
        entity.setInterval(schedule.getInterval());
        entity.setTimeUnit(schedule.getTimeUnit());
        Mail mail = dto.getMail();
        entity.setReceiver(mail.getReceiver());
        Report report = mail.getSubject();
        entity.setType(report.getType());
        entity.setParams(report.getParams());
        entity.setUsername(userService.getUserDetails().getUsername());
        return entity;
    }
}
