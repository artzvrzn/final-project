package by.itacademy.mail.view.api;

import by.itacademy.mail.model.Mail;

public interface IMailService<T> {

    void send(Mail<T> params);
}
