package by.itacademy.mail.view.api;

import by.itacademy.mail.model.Mail;

public interface MailService<T> {

    void send(Mail<T> params);
}
