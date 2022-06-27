package by.itacademy.telegram.view.api;

import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.MenuState;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.UUID;

public interface ChatService {

    Chat get(Message message);

    Chat get(String id);

    void create(Chat chat);

    void update(String chatId, Chat chat);

    void updateState(String chatId, MenuState state);

    void updateAccount(String chatId, UUID accountId);

    void updateOperation(String chatId, UUID operationId);
}
