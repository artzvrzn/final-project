package by.itacademy.mail.exception;


import by.itacademy.mail.model.error.ResponseError;

public class ResponseException extends IllegalArgumentException {

    private final ResponseError responseError;

    public ResponseException(String message, ResponseError responseError) {
        super(message);
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
