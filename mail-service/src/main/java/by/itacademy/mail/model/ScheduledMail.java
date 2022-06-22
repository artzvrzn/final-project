package by.itacademy.mail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ScheduledMail<T> {

    private UUID id;
    private Mail<T> mail;
}
