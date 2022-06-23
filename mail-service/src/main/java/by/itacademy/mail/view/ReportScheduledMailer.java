package by.itacademy.mail.view;

import by.itacademy.mail.model.FileData;
import by.itacademy.mail.model.Mail;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.model.QueueMail;
import by.itacademy.mail.view.api.CommunicatorService;
import by.itacademy.mail.view.api.ScheduledMailer;
import by.itacademy.mail.view.api.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Log4j2
@Component
public class ReportScheduledMailer implements ScheduledMailer<Report> {

    private final Queue<QueueMail<Report>> awaiting = new LinkedBlockingQueue<>();
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StorageService storageService;
    @Autowired
    private CommunicatorService<Report> communicatorService;

    @Override
    public void addToQueue(QueueMail<Report> mail) {
        awaiting.add(mail);
    }

    @Scheduled(fixedRate = 1000)
    private void executeTasks() {
        int size = awaiting.size();
        for (int i = 0; i <= size; i++) {
            QueueMail<Report> qm = awaiting.poll();
            if (qm == null) {
                continue;
            }
            if (!communicatorService.isAvailable(qm.getId())) {
                log.info("Report {} has not yet completed ", qm.getId());
                awaiting.add(qm);
            } else {
                send(qm.getId(), qm.getMail());
            }
        }
    }

    private void send(UUID id, Mail<Report> mail) {
        try {
            Future<FileData> future = storageService.download(id);
            FileData file = future.get();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(mail.getReceiver());
            helper.setSubject("Отчет по операциям");
            helper.setText("Сформированный отчет: ");
            ByteArrayDataSource attachment = new ByteArrayDataSource(
                    new ByteArrayInputStream(file.getContent()), file.getMediaType().toString());
            helper.addAttachment(file.getFilename(), attachment);
            mailSender.send(message);
            log.info("Message has been successfully sent");
        } catch (Exception e) {
            log.error("Error occurred while sending mail: {}", e.getMessage());
            throw new IllegalStateException("Error during sending a mail", e);
        }
    }
}
