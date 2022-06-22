package by.itacademy.mail.view.api;

import by.itacademy.mail.model.ScheduledMail;

public interface ScheduledMailer<T> {

   void addToQueue(ScheduledMail<T> scheduledMail);
}
