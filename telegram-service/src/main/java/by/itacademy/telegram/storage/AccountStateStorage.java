package by.itacademy.telegram.storage;

import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Authentication;
import by.itacademy.telegram.storage.api.StateStorage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountStateStorage implements StateStorage<Account> {

    private final Map<String, Account> stateStorageMap = new ConcurrentHashMap<>();

    @Override
    public Account get(String chatId) {
        return stateStorageMap.get(chatId);
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
