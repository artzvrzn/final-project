package by.itacademy.mail.view.api;

import java.util.UUID;

public interface CommunicatorService<T> {

    UUID postRequest(T request);

    boolean isAvailable(UUID id);
}
