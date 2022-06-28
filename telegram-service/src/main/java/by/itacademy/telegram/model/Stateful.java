package by.itacademy.telegram.model;

import java.time.LocalDateTime;

public interface Stateful {

    int getState();

    LocalDateTime getInstantiated();
}
