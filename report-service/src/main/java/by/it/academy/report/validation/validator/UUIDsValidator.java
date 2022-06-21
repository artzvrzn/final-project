package by.it.academy.report.validation.validator;

import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.validation.validator.api.IValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.it.academy.report.utils.Messages.*;

public abstract class UUIDsValidator implements IValidator<List<UUID>> {

    protected final String fieldKey;

    protected UUIDsValidator(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    @Override
    public List<Violation> validate(List<UUID> uuids) {
        List<Violation> violations = new ArrayList<>();
        if (uuids != null && !uuids.isEmpty()) {
            for (UUID id: uuids) {
                if (id == null) {
                    violations.add(new Violation(fieldKey, INVALID_FORMAT.getText()));
                    return violations;
                }
                sendRequest(id, violations);
            }
        } else if (uuids == null) {
            violations.add(new Violation(fieldKey, IS_NULL.getText()));
        } else {
            violations.add(new Violation(fieldKey, IS_EMPTY.getText()));
        }
        return violations;
    }

    protected abstract void sendRequest(UUID id, List<Violation> violations);
}
