package by.it.academy.report.validation.validator;

import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.validation.validator.api.IValidator;
import by.it.academy.report.validation.validator.api.ParamsValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("BY_DATE")
public class ByDateParamsValidator implements ParamsValidator {

    private final ByCategoryParamsValidator byCategoryParamsValidator;

    public ByDateParamsValidator(ByCategoryParamsValidator byCategoryParamsValidator) {
        this.byCategoryParamsValidator = byCategoryParamsValidator;
    }

    @Override
    public List<Violation> validate(Map<String, Object> params) {
        return byCategoryParamsValidator.validate(params);
    }
}
