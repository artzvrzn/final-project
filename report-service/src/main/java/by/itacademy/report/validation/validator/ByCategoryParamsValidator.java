package by.itacademy.report.validation.validator;

import by.itacademy.report.controller.advice.error.Violation;
import by.itacademy.report.validation.validator.api.ParamsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static by.itacademy.report.utils.Messages.*;

@Log4j2
@Component("BY_CATEGORY")
public class ByCategoryParamsValidator implements ParamsValidator {

    private static final String ACCOUNT_KEY = "accounts";
    private static final String FROM_KEY = "from";
    private static final String TO_KEY = "to";
    private static final String CATEGORY_KEY = "categories";
    @Autowired
    private AccountsValidator accountsValidator;
    @Autowired
    private CategoriesValidator categoriesValidator;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public List<Violation> validate(Map<String, Object> params) {
        List<Violation> violations = new ArrayList<>();
        List<UUID> accounts = readUUIDs(ACCOUNT_KEY, params, violations);
        LocalDate from = readLocalDate(FROM_KEY, params, violations);
        LocalDate to = readLocalDate(TO_KEY, params, violations);
        List<UUID> categories = readUUIDs(CATEGORY_KEY, params, violations);
        if (!violations.isEmpty()) {
            return violations;
        }
        violations.addAll(accountsValidator.validate(accounts));
        if (!violations.isEmpty()) {
            return violations;
        }
        violations.addAll(categoriesValidator.validate(categories));
        return violations;
    }

    private LocalDate readLocalDate(String key, Map<String, Object> paramsMap, List<Violation> violations) {
        return readValue(key, paramsMap, violations, LocalDate.class);
    }

    private List<UUID> readUUIDs(String key, Map<String, Object> paramsMap, List<Violation> violations) {
        return readIterableValues(key, paramsMap, violations, UUID.class);
    }

    private <T> List<T> readIterableValues
            (String key, Map<String, Object> paramsMap, List<Violation> violations, Class<T> clazz) {
        try {
            Object value = paramsMap.get(key);
            if (value == null) {
                violations.add(new Violation(key, IS_NULL.getText()));
                return null;
            }
            List<T> result = mapper.convertValue(
                    paramsMap.get(key), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            if (result == null) {
                violations.add(new Violation(key, IS_NULL.getText()));
            } else if (result.isEmpty()) {
                violations.add(new Violation(key, IS_EMPTY.getText()));
            }
            return result;
        } catch (IllegalArgumentException exc) {
            log.error("Failed to read {}: {}", key, exc.getMessage());
            violations.add(new Violation(key, INVALID_FORMAT.getText()));
            return null;
        }
    }

    private <T> T readValue(String key, Map<String, Object> paramsMap, List<Violation> violations, Class<T> clazz) {
        try {
            Object value = paramsMap.get(key);
            if (value == null) {
                violations.add(new Violation(key, IS_NULL.getText()));
                return null;
            }
            T result = mapper.convertValue(value, clazz);
            if (result == null) {
                violations.add(new Violation(key, IS_NULL.getText()));
            }
            if (result instanceof CharSequence) {
                if (((CharSequence) result).length() == 0) {
                    violations.add(new Violation(key, IS_NULL.getText()));
                }
            }
            return result;
        } catch (IllegalArgumentException exc) {
            log.error("Failed to read {}: {}", key, exc.getMessage());
            violations.add(new Violation(key, INVALID_FORMAT.getText()));
            return null;
        }
    }
}
