package by.itacademy.report.view;

import by.itacademy.report.controller.advice.error.Violation;
import by.itacademy.report.exception.ValidationException;
import by.itacademy.report.model.ReportType;
import by.itacademy.report.validation.validator.ParamsValidatorFactory;
import by.itacademy.report.view.api.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportValidationService implements ValidationService {

    @Autowired
    private ParamsValidatorFactory validatorFactory;

    @Override
    public void validate(ReportType type, Map<String, Object> params) {
        List<Violation> violations = validatorFactory.getValidator(type).validate(params);
        if (!violations.isEmpty()) {
            throw new ValidationException("Validation failed", violations);
        }
    }
}
