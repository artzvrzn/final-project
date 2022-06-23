package by.itacademy.report.controller.rest;

import by.itacademy.report.model.ReportType;
import by.itacademy.report.view.api.UserService;
import by.itacademy.report.view.api.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/validate/")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping(value = {"{type}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void validate(@PathVariable("type") ReportType type,
                         @RequestBody Map<String, Object> params) {
        validationService.validate(type, params);
    }
}
