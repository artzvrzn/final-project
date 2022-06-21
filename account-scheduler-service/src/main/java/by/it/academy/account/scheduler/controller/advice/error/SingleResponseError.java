package by.it.academy.account.scheduler.controller.advice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SingleResponseError {

    private final String logref;
    private final String message;

}
