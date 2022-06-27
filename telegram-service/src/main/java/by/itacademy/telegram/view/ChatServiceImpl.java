package by.itacademy.telegram.view;

import by.itacademy.telegram.dao.api.ChatRepository;
import by.itacademy.telegram.dao.entity.ChatEntity;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.view.api.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ConversionService conversionService;

    @Override
    public Chat get(Message message) {
        if (message == null) {
            log.error("message is null");
            throw new IllegalArgumentException("Message is null");
        }
        String chatId = message.getChatId().toString();
        Optional<ChatEntity> optional = chatRepository.findById(chatId);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", chatId);
            return null;
        }
        return conversionService.convert(optional.get(), Chat.class);
    }

    @Override
    public Chat get(String id) {
        Optional<ChatEntity> optional = chatRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", id);
            throw new IllegalArgumentException("Chat not found");
        }
        return conversionService.convert(optional.get(), Chat.class);
    }

    @Override
    public void create(Chat chat) {
        chatRepository.save(conversionService.convert(chat, ChatEntity.class));
    }

    @Override
    public void update(String chatId, Chat chat) {
        Optional<ChatEntity> optional = chatRepository.findById(chatId);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", chatId);
            throw new IllegalArgumentException("Chat not found");
        }
        ChatEntity entity = optional.get();
        entity.setMenuState(chat.getState());
        entity.setJwtToken(chat.getJwtToken());
    }

    @Override
    public void updateState(String chatId, MenuState state) {
        Optional<ChatEntity> optional = chatRepository.findById(chatId);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", chatId);
            throw new IllegalArgumentException("Chat not found");
        }
        ChatEntity entity = optional.get();
        entity.setMenuState(state);
    }

    @Override
    public void updateAccount(String chatId, UUID accountId) {
        Optional<ChatEntity> optional = chatRepository.findById(chatId);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", chatId);
            throw new IllegalArgumentException("Chat not found");
        }
        ChatEntity entity = optional.get();
        entity.setMenuState(MenuState.ACCOUNT);
        entity.setChosenAccount(accountId);
        chatRepository.save(entity);
    }

    @Override
    public void updateOperation(String chatId, UUID operationId) {
        Optional<ChatEntity> optional = chatRepository.findById(chatId);
        if (optional.isEmpty()) {
            log.error("chat with id {} not found", chatId);
            throw new IllegalArgumentException("Chat not found");
        }
        ChatEntity entity = optional.get();
        entity.setMenuState(MenuState.OPERATION);
        entity.setChosenOperation(operationId);
        chatRepository.save(entity);
    }
}
