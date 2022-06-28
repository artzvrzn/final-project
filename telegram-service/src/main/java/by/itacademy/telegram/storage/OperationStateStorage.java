package by.itacademy.telegram.storage;

import by.itacademy.telegram.model.Authentication;
import by.itacademy.telegram.model.Operation;
import by.itacademy.telegram.storage.api.StateStorage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OperationStateStorage implements StateStorage<Operation> {

    private final Map<String, Operation> stateStorageMap = new ConcurrentHashMap<>();

    @Override
    public Operation get(String chatId) {
        return stateStorageMap.get(chatId);
    }

    @Override
    public Map<String, Operation> getAll() {
        return Map.copyOf(stateStorageMap);
    }

    @Override
    public void add(String chatId, Operation operation) {
        stateStorageMap.put(chatId, operation);
    }

    @Override
    public void delete(String chatId) {
        stateStorageMap.remove(chatId);
    }
}
