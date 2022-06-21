package by.it.academy.report.validation.validator.api;

import by.it.academy.report.controller.advice.error.Violation;

import java.util.List;

public interface IValidator<T> {

    List<Violation> validate(T object);
}
