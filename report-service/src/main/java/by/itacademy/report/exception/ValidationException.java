package by.itacademy.report.exception;

import by.itacademy.report.controller.advice.error.Violation;

import java.util.Collections;
import java.util.List;

public class ValidationException extends IllegalArgumentException {

    private final List<Violation> violations;

    public ValidationException(String message, List<Violation> violations) {
        super(message);
        this.violations = violations;
    }

    public ValidationException(String message, List<Violation> violations, Throwable throwable) {
        super(message, throwable);
        this.violations = violations;
    }

    public ValidationException(String message) {
        super(message);
        this.violations = Collections.emptyList();
    }

    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
        this.violations = Collections.emptyList();
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
