package by.it.academy.report.validation.validator;

import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.exception.ValidationException;
import by.it.academy.report.validation.validator.api.IValidator;
import by.it.academy.report.validation.validator.api.ParamsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static by.it.academy.report.utils.Messages.*;

@Log4j2
@Component("BALANCE")
public class BalanceParamsValidator implements ParamsValidator {
    private static final String ACCOUNT_KEY = "accounts";
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AccountsValidator accountsValidator;

    @Override
    public List<Violation> validate(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new ValidationException("params is empty");
        }
        if (params.size() > 1) {
            throw new ValidationException("Illegal params were passed");
        }
        List<Violation> violations = new ArrayList<>();
        List<UUID> ids = readAccounts(params, violations);
        if (!violations.isEmpty()) {
            return violations;
        }
        violations.addAll(accountsValidator.validate(ids));
        return violations;
    }

    private List<UUID> readAccounts(Map<String, Object> paramsMap, List<Violation> violations) {
        try {
            Object value = paramsMap.get(ACCOUNT_KEY);
            if (value == null) {
                violations.add(new Violation(ACCOUNT_KEY, IS_NULL.getText()));
                return null;
            }
            List<UUID> result = mapper.convertValue(
                    value, mapper.getTypeFactory().constructCollectionType(List.class, UUID.class));
            if (result == null) {
                violations.add(new Violation(ACCOUNT_KEY, IS_NULL.getText()));
            } else if (result.isEmpty()) {
                violations.add(new Violation(ACCOUNT_KEY, IS_EMPTY.getText()));
            }
            return result;
        } catch (IllegalArgumentException exc) {
            log.error("Failed to read accounts:" + exc.getMessage());
            violations.add(new Violation(ACCOUNT_KEY, INVALID_FORMAT.getText()));
            return null;
        }
    }
}
