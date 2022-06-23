package by.itacademy.mail.view.api;

import by.itacademy.mail.model.QueueMail;

public interface ScheduledMailer<T> {

   void addToQueue(QueueMail<T> queueMail);
}
