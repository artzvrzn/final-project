package by.it.academy.account.scheduler.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Violation {

    private final String field;
    private final String message;
}
