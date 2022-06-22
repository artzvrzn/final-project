package by.itacademy.mail.view;

import by.itacademy.mail.model.Mail;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.model.ScheduledMail;
import by.itacademy.mail.view.api.CommunicatorService;
import by.itacademy.mail.view.api.MailService;
import by.itacademy.mail.view.api.ScheduledMailer;
import by.itacademy.mail.view.api.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportMailService implements MailService<Report> {

    @Autowired
    private CommunicatorService<Report> communicatorService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ScheduledMailer<Report> scheduledMailer;

    @Override
    public void send(Mail<Report> mail) {
        UUID id = communicatorService.postRequest(mail.getSubject());
        ScheduledMail<Report> scheduledMail = new ScheduledMail<>(id, mail);
        scheduledMailer.addToQueue(scheduledMail);
    }
}
