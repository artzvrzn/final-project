package by.itacademy.account.scheduler.controller.advice;

import by.itacademy.account.scheduler.controller.advice.error.MultipleResponseError;
import by.itacademy.account.scheduler.controller.advice.error.SingleResponseError;
import by.itacademy.account.scheduler.controller.advice.error.Violation;
import by.itacademy.account.scheduler.exception.RecordNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
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
        List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.error("{}: {}", exception.getClass().getSimpleName(), violations);
        return new MultipleResponseError(STRUCTURED_ERROR, violations);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError validationHandler(BindException exception) {
        List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.error("{}: {}", exception.getClass().getSimpleName(), violations);
        return new SingleResponseError(SINGLE_ERROR, INVALID_PARAMETERS);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MultipleResponseError validationHandler(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations().stream()
                .map(error -> new Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        log.error("{}: {}", exception.getClass().getSimpleName(), violations);
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

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<?> validationHandler(ValidationException exception) {
        ResponseEntity<?> response;
        String exceptionName = exception.getCause().getClass().getSimpleName();
        switch (exceptionName) {
            case "AccessDeniedException":
                response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
                break;
            case "SecurityException":
                response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                break;
            default:
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return response;
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
