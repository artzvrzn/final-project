package by.itacademy.account.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleResponseError {

    private final String logref;
    private final List<Violation> errors;

}
