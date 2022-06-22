package by.itacademy.classifier.controller.advice;

import by.itacademy.classifier.exception.RecordNotFoundException;
import by.itacademy.classifier.model.error.MultipleResponseError;
import by.itacademy.classifier.model.error.SingleResponseError;
import by.itacademy.classifier.model.error.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class ExceptionAdvice {
    private final String STRUCTURED_ERROR = "structured_error";
    private final String SINGLE_ERROR = "error";
    private final String INVALID_PARAMETERS = "request contains invalid parameters";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MultipleResponseError methodArgumentValidHandler(MethodArgumentNotValidException exception) {
        log.error(exception.getBindingResult().getFieldErrors());
        List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new MultipleResponseError(STRUCTURED_ERROR, violations);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError validationHandler(BindException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new SingleResponseError(SINGLE_ERROR, INVALID_PARAMETERS);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MultipleResponseError validationHandler(ConstraintViolationException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        List<Violation> violations = exception.getConstraintViolations().stream()
                .map(error -> new Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        return new MultipleResponseError(STRUCTURED_ERROR, violations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError validationHandler(HttpMessageNotReadableException exception) {
        return commonHandler(exception, INVALID_PARAMETERS);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError illegalArgument(IllegalArgumentException exception) {
        return commonHandler(exception);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError recordNotFoundHandler(RecordNotFoundException exception) {
        return commonHandler(exception);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public void unauthorizedHandler(SecurityException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public void forbiddenHandler(AccessDeniedException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError illegalState(Exception exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new SingleResponseError(SINGLE_ERROR, "unable to process the request");
    }

    private SingleResponseError commonHandler(RuntimeException exception, String message) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new SingleResponseError(SINGLE_ERROR, message);
    }

    private SingleResponseError commonHandler(RuntimeException exception) {
        return commonHandler(exception, exception.getMessage());
    }
}
