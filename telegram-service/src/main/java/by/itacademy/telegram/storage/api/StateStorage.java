package by.itacademy.telegram.storage.api;

import by.itacademy.telegram.model.Stateful;

public interface StateStorage<T extends Stateful> {

    T get(String id);

    void add(String id, T object);

    void delete(String id);
}
