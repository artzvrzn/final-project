package by.itacademy.report.controller.advice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Violation {

    private final String field;
    private final String message;
}
