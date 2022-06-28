package by.itacademy.telegram.storage;

import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.storage.api.StateStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.temporal.ChronoUnit.MILLIS;

@Component
public class AccountStateStorage implements StateStorage<Account> {

    private final Map<String, Account> stateStorageMap = new ConcurrentHashMap<>();

    @Override
    public Account get(String chatId) {
        return stateStorageMap.get(chatId);
    }

    @Override
    public Map<String, Account> getAll() {
        return Map.copyOf(stateStorageMap);
    }

    @Override
    public void add(String chatId, Account account) {
        stateStorageMap.put(chatId, account);
    }

    @Override
    public void delete(String chatId) {
        stateStorageMap.remove(chatId);
    }
}
