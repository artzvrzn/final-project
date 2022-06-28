package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Category;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.Operation;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.storage.OperationStateStorage;
import by.itacademy.telegram.utils.DateTimeFormatterUtil;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static by.itacademy.telegram.model.constant.Reply.*;

@Log4j2
@Component
public class UpdateOperationMessageHandler implements MessageHandler {

    @Autowired
    private OperationStateStorage stateStorage;
    @Autowired
    private ChatService chatService;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private FinanceTelegramBot bot;
    @Autowired
    private CommunicatorService communicatorService;

    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        log.info("chat {} message {}", chatId, input);
        if (input.equals(ButtonType.SUB_GET_BACK.getText())) {
            stateStorage.delete(chatId);
            chatService.updateState(chatId, MenuState.OPERATION);
            return basicReplyAccountMenu(chatId, HEADER_OPERATION.getText());
        }
        if (input.equals(ButtonType.SUB_START_AGAIN.getText())) {
            stateStorage.delete(chatId);
            bot.sendMessage(basicReply(chatId, NEW_ATTEMPT.getText()));
            return basicReply(chatId, OPERATION_UPDATE_DATE.getText());
        }
        Operation operation = stateStorage.get(chat.getId());
        if (operation == null) {
            UUID operationId = chat.getChosenOperation();
            operation = communicatorService.getOperation(operationId, chat.getId());
            operation.setState(0);
            stateStorage.add(chat.getId(), operation);
        }
        handleOperation(operation, message, chat);
        generateReply(operation, chatId);
        return null;
    }

    private void generateReply(Operation operation, String chatId) {
        switch (operation.getState()) {
            case 0:
                bot.sendMessage(basicReply(chatId, OPERATION_UPDATE_DATE.getText()));
                break;
            case 1:
                bot.sendMessage(basicReply(chatId, OPERATION_UPDATE_DESCRIPTION.getText()));
                break;
            case 2:
                bot.sendMessage(basicReply(chatId, OPERATION_UPDATE_CATEGORY.getText()));
                bot.sendMessage(basicReply(chatId, HandlerUtils.getAvailableCategories(communicatorService, chatId)));
                break;
            case 3:
                bot.sendMessage(basicReply(chatId, OPERATION_UPDATE_VALUE.getText()));
                break;
            default:
        }
    }

    private void handleOperation(Operation operation, Message message, Chat chat) {
        String input = message.getText();
        String chatId = chat.getId();
        if (ButtonType.contains(input)) {
            return;
        }
        log.info("chat id {}, account stage {}, input {}",
                chatId, operation.getState(), input);
        switch (operation.getState()) {
            case 0:
                setDate(operation, input, chat);
                break;
            case 1:
                if (!isSlash(input)) operation.setDescription(input);
                operation.setState(2);
                break;
            case 2:
                setCategory(operation, input, chat);
                break;
            case 3:
                setValue(operation, input, chat);
                break;
            case 4:
                bot.sendMessage(basicReply(chatId, OPERATION_SUCCESS.getText()));
                break;
            case 5:
                bot.sendMessage(basicReply(chatId, OPERATION_FAILED.getText()));
                bot.sendMessage(basicReply(chatId, NEW_ATTEMPT_QUESTION.getText()));
                break;
            default:
                bot.sendMessage(basicReply(chatId, UNKNOWN_COMMAND.getText()));
        }
    }

    private void setDate(Operation operation, String input, Chat chat) {
        if (isSlash(input)) {
            operation.setState(1);
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(input, DateTimeFormatterUtil.getUniversalDateFormatter());
        } catch (DateTimeException e) {
            log.error("chat id {}, input {}, exception {}", chat.getId(), input, e.getMessage());
            bot.sendMessage(basicReply(chat.getId(), WRONG_DATE_FORMAT.getText()));
            return;
        }
        operation.setDate(date);
        operation.setState(1);
    }

    private void setCategory(Operation operation, String input, Chat chat) {
        if (isSlash(input)) {
            operation.setState(3);
            return;
        }
        Category category = communicatorService.getCategory(input, chat.getId());
        if (category == null) {
            log.error("chat id {}, input {}, wrong category", chat.getId(), input);
            bot.sendMessage(basicReply(chat.getId(), OPERATION_WRONG_CATEGORY.getText()));
            return;
        }
        operation.setCategory(category.getId());
        operation.setState(3);
    }

    private void setValue(Operation operation, String input, Chat chat) {
        if (!isSlash(input)) {
            BigDecimal value;
            try {
                value = new BigDecimal(input);
            } catch (NumberFormatException e) {
                log.error("chat id {}, input {}, exception {}", chat.getId(), input, e.getMessage());
                bot.sendMessage(basicReply(chat.getId(), OPERATION_WRONG_VALUE.getText()));
                return;
            }
            operation.setValue(value);
        }
        try {
            Account account = communicatorService.getAccount(chat.getChosenAccount(), chat.getId());
            if (account == null) {
                bot.sendMessage(basicReply(chat.getId(), OPERATION_UPDATE_FAILED.getText()));
                bot.sendMessage(basicReply(chat.getId(), NEW_ATTEMPT_QUESTION.getText()));
                operation.setState(5);
                return;
            }
            operation.setCurrency(account.getCurrency());
            communicatorService.updateOperation(account.getUuid(), operation, chat.getId());
            bot.sendMessage(basicReply(chat.getId(), OPERATION_UPDATE_SUCCESS.getText()));
            operation.setState(4);
        } catch (HttpStatusCodeException exc) {
            operation.setState(5);
            bot.sendMessage(basicReply(chat.getId(), OPERATION_UPDATE_FAILED.getText()));
            bot.sendMessage(basicReply(chat.getId(), NEW_ATTEMPT_QUESTION.getText()));
        }
    }

    private boolean isSlash(String text) {
        return text.equals("/");
    }

    private SendMessage basicReplyAccountMenu(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.OPERATION));
    }

    private SendMessage basicReply(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.OPERATION_UPDATE));
    }
}
