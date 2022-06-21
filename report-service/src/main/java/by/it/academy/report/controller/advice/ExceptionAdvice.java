package by.it.academy.report.controller.advice;

import by.it.academy.report.controller.advice.error.MultipleResponseError;
import by.it.academy.report.controller.advice.error.SingleResponseError;
import by.it.academy.report.controller.advice.error.Violation;
import by.it.academy.report.exception.RecordNotFoundException;
import by.it.academy.report.exception.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class ExceptionAdvice {

    private final String STRUCTURED_ERROR = "structured_error";
    private final String SINGLE_ERROR = "error";
    private final String INVALID_PARAMETERS = "request contains invalid parameters";

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationHandler(ValidationException e) {
        List<Violation> violations = e.getViolations();
        if (violations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SingleResponseError(SINGLE_ERROR, e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MultipleResponseError(STRUCTURED_ERROR, violations));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResponseError recordNotFoundHandler(RecordNotFoundException e) {
        return new SingleResponseError(SINGLE_ERROR, e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void securityExceptionHandler(SecurityException e) {
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void accessDeniedExceptionHandler(AccessDeniedException e) {
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void noSuchElementHandler(NoSuchElementException e) {
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError notReadableHandler(HttpMessageNotReadableException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new SingleResponseError(SINGLE_ERROR, INVALID_PARAMETERS);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SingleResponseError unsupportedMediaTypeHandler(HttpMediaTypeNotSupportedException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return new SingleResponseError(SINGLE_ERROR, INVALID_PARAMETERS);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResponseError exceptionHandler(RuntimeException e) {
        log.error("Error occurred: {}", e.getMessage());
        return new SingleResponseError(SINGLE_ERROR, INVALID_PARAMETERS);
    }

}
