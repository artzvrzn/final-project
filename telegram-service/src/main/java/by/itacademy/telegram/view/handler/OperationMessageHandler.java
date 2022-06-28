package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.*;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.storage.OperationStateStorage;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.format.DateTimeFormatter;

import static by.itacademy.telegram.model.constant.Reply.*;

@Log4j2
@Component
public class OperationMessageHandler implements MessageHandler {

    @Autowired
    private OperationStateStorage stateStorage;
    @Autowired
    private ChatService chatService;
    @Autowired
    private FinanceTelegramBot bot;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private MessageHandlers factory;
    @Autowired
    private CommunicatorService communicatorService;

    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        log.info("chat {} message {}", chatId, input);

        if (input.equals(ButtonType.OPERATION_GET_INFO.getText())) {
            return getInfo(chat);
        }
        if (input.equals(ButtonType.OPERATION_UPDATE.getText())) {
            chatService.updateState(chatId, MenuState.OPERATION_UPDATE);
            bot.sendMessage(basicReplyNewState(chatId, HEADER_OPERATION_UPDATE.getText(), MenuState.OPERATION_UPDATE));
            return factory.handle(message);
        }
        if (input.equals(ButtonType.OPERATION_DELETE.getText())) {
            return deleteOperation(chat);
        }
        if (input.equals(ButtonType.SUB_GET_BACK.getText())) {
            stateStorage.delete(chat.getId());
            chat.setChosenOperation(null);
            chat.setState(MenuState.ACCOUNT);
            chatService.update(chat.getId(), chat);
            return basicReplyNewState(chatId, HEADER_ACCOUNT.getText(), MenuState.ACCOUNT);
        }
        return null;
    }

    private SendMessage getInfo(Chat chat) {
        Operation operation = communicatorService.getOperation(chat.getChosenOperation(), chat.getId());
        Currency currency = communicatorService.getCurrency(operation.getCurrency(), chat.getId());
        Category category = communicatorService.getCategory(operation.getCategory(), chat.getId());
        String text = String.format(
                "Id: %s\nДата: %s\nОписание: %s\nКатегория: %s\nСумма: %s\nВалюта: %s\n",
                operation.getUuid(),
                operation.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                operation.getDescription(),
                category.getTitle(),
                operation.getValue(),
                currency.getTitle());
        return basicReply(chat.getId(), text);
    }

    private SendMessage deleteOperation(Chat chat) {
        String chatId = chat.getId();
        Operation operation = communicatorService.getOperation(chat.getChosenOperation(), chatId);
        if (operation == null) {
            return basicReply(chatId, OPERATION_DELETE_FAILED.getText());
        }
        try {
            communicatorService.deleteOperation(chat.getChosenAccount(), operation, chatId);
            stateStorage.delete(chat.getId());
            chat.setChosenOperation(null);
            chat.setState(MenuState.ACCOUNT);
            chatService.update(chat.getId(), chat);
            return basicReplyNewState(chatId, OPERATION_DELETE_SUCCESS.getText(), MenuState.ACCOUNT);
        } catch (Exception e) {
            return basicReply(chatId, OPERATION_DELETE_FAILED.getText());
        }
    }

    private SendMessage basicReply(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(MenuState.OPERATION).get());
        return message;
    }

    private SendMessage basicReplyNewState(String chatId, String text, MenuState state) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(state).get());
        return message;
    }
}
