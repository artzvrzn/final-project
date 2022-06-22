package by.itacademy.mail.controller;

import by.itacademy.mail.model.Mail;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.view.api.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mail/report")
public class ReportMailController {

    @Autowired
    private MailService<Report> service;

    @PostMapping(value = {"", "/"})
    @ResponseStatus(HttpStatus.OK)
    public void sendMail(@RequestBody @Valid Mail<Report> mailParams) {
        service.send(mailParams);
    }
}
