package by.itacademy.telegram.storage.api;

import by.itacademy.telegram.model.Stateful;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Map;

public interface StateStorage<T extends Stateful> {

    T get(String id);

    Map<String, T> getAll();

    void add(String id, T object);

    void delete(String id);


}
