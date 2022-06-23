package by.itacademy.mail.view.api;

import by.itacademy.mail.model.Mail;

public interface ValidationService<T> {

    void validate(Mail<T> body);
}
