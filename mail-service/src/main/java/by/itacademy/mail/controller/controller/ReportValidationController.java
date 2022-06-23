package by.itacademy.mail.controller.controller;

import by.itacademy.mail.model.Mail;
import by.itacademy.mail.model.Report;
import by.itacademy.mail.view.api.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/mail/validate/report/")
public class ReportValidationController {

    @Autowired
    private ValidationService<Report> validationService;

    @PostMapping(value = {"{type}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void validate(@RequestBody @Valid Mail<Report> mailParams) {
        validationService.validate(mailParams);
    }
}
