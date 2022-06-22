package by.itacademy.report.validation.validator.api;

import by.itacademy.report.controller.advice.error.Violation;

import java.util.List;

public interface IValidator<T> {

    List<Violation> validate(T object);
}
