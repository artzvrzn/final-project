package by.it.academy.classifier.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SingleResponseError {

    private final String logref;
    private final String message;
}
