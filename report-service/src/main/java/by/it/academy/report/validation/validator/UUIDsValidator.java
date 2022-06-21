package by.it.academy.report.validation.validator;

import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.validation.validator.api.IValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                sendRequest(id, violations);
            }
        } else if (uuids == null) {
            violations.add(new Violation(fieldKey, "is null"));
        } else {
            violations.add(new Violation(fieldKey, "is empty"));
        }
        return violations;
    }

    protected abstract void sendRequest(UUID id, List<Violation> violations);
}
