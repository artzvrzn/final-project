package by.itacademy.mail.controller.advice;

import by.itacademy.mail.exception.ResponseException;
import by.itacademy.mail.model.error.MultipleResponseError;
import by.itacademy.mail.model.error.ResponseError;
import by.itacademy.mail.model.error.SingleResponseError;
import by.itacademy.mail.model.error.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class ExceptionAdvice {


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MultipleResponseError validationHandler(ConstraintViolationException exception) {
        log.error("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        List<Violation> violations = exception.getConstraintViolations().stream()
                .map(error -> new Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        MultipleResponseError error = new MultipleResponseError();
        error.setLogref("structured_error");
        error.setErrors(violations);
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MultipleResponseError methodArgumentValidHandler(MethodArgumentNotValidException exception) {
        log.error(exception.getBindingResult().getFieldErrors());
        List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        MultipleResponseError error = new MultipleResponseError();
        error.setLogref("structured_error");
        error.setErrors(violations);
        return error;
    }

    @ExceptionHandler(ResponseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseError responseHandler(ResponseException exc) {
        log.error("request returned error: {}", exc.getMessage());
        return exc.getResponseError();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public void forbiddenHandler(AccessDeniedException exc) {
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public void unauthorizedHandler(SecurityException exc) {
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseError responseHandler(HttpStatusCodeException exc) {
        log.error("request returned error: {}", exc.getMessage());
        SingleResponseError error = new SingleResponseError();
        error.setLogref("error");
        error.setMessage("unable to process the request");
        return error;
    }
}
