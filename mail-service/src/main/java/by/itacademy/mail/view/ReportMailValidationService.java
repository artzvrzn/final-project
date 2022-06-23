package by.itacademy.mail.view;

import by.itacademy.mail.model.Mail;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.view.api.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportMailValidationService implements ValidationService<Report> {

    @Autowired
    private ReportServiceCommunicator communicator;

    @Override
    public void validate(Mail<Report> mail) {
        communicator.sendValidationRequest(mail.getSubject());
    }
}
