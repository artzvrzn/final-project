package by.itacademy.report.validation.validator;

import by.itacademy.report.model.ReportType;
import by.itacademy.report.validation.validator.api.ParamsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ParamsValidatorFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public ParamsValidator getValidator(ReportType type) {
        switch (type) {
            case BALANCE:
                return applicationContext.getBean(BalanceParamsValidator.class);
            case BY_DATE:
                return applicationContext.getBean(ByDateParamsValidator.class);
            case BY_CATEGORY:
                return applicationContext.getBean(ByCategoryParamsValidator.class);
            default:
                throw new IllegalArgumentException("Wrong report type");
        }
    }
}
