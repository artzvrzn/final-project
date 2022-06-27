package by.itacademy.telegram.storage;

import by.itacademy.telegram.model.Authentication;
import by.itacademy.telegram.storage.api.StateStorage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthenticationStateStorage implements StateStorage<Authentication> {

    private final Map<String, Authentication> stateStorageMap = new ConcurrentHashMap<>();

    @Override
    public Authentication get(String id) {
        return stateStorageMap.get(id);
    }

    @Override
    public void add(String id, Authentication authentication) {
        stateStorageMap.put(id, authentication);
    }

    @Override
    public void delete(String id) {
        stateStorageMap.remove(id);
    }
}
