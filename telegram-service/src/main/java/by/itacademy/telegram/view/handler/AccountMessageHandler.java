package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.*;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import by.itacademy.telegram.view.keyboard.OperationInlineKeyboard;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static by.itacademy.telegram.model.constant.Reply.*;

@Log4j2
@Component
public class AccountMessageHandler implements MessageHandler {

    @Autowired
    private FinanceTelegramBot bot;
    @Autowired
    private ChatService chatService;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private MessageHandlerFactory factory;
    @Autowired
    private CommunicatorService communicatorService;

    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        log.info("chat {} message {}", chatId, input);
        if (input.equals(ButtonType.ACCOUNT_GET_INFO.getText())) {
            return getInfo(chat);
        }
        if (input.equals(ButtonType.ACCOUNT_UPDATE_ACCOUNT.getText())) {
            chatService.updateState(chatId, MenuState.ACCOUNT_UPDATE);
            bot.sendMessage(basicReplyNewState(chatId, HEADER_ACCOUNT_UPDATE.getText(), MenuState.ACCOUNT_UPDATE));
            return factory.delegate(message);
        }
        if (input.equals(ButtonType.ACCOUNT_GET_BALANCE.getText())) {
            return getBalance(chat);
        }
        if (input.equals(ButtonType.ACCOUNT_CREATE_OPERATION.getText())) {
            chatService.updateState(chatId, MenuState.OPERATION_CREATE);
            bot.sendMessage(basicReplyNewState(chatId, HEADER_OPERATION_CREATE.getText(), MenuState.OPERATION_CREATE));
            return factory.delegate(message);
        }
        if (input.equals(ButtonType.ACCOUNT_CHOOSE_OPERATION.getText())) {
            return chooseReply(chatId);
        }
        if (input.equals(ButtonType.ACCOUNT_GET_OPERATIONS.getText())) {
            getOperations(chatId);
            return null;
        }
        if (input.equals(ButtonType.SUB_GET_BACK.getText())) {
            chatService.updateState(chat.getId(), MenuState.MAIN);
            return basicReplyNewState(chat.getId(), HEADER_MAIN.getText(), MenuState.MAIN);
        }
        return null;
    }

    private void getOperations(String chatId) {
        Map<UUID, Currency> knownCurrencies = new HashMap<>();
        Map<UUID, Category> knownCategories = new HashMap<>();
        try {
            List<List<Operation>> partitionedOperations = partitionedOperations(chatId);
            if (partitionedOperations.isEmpty()) {
                bot.sendMessage(basicReply(chatId, NO_OPERATIONS.getText()));
                return;
            }
            for (List<Operation> part: partitionedOperations) {
                bot.sendMessage(basicReply(
                        chatId, buildOperationsString(part, chatId, knownCurrencies, knownCategories)));
            }
        } catch (Exception e) {
            log.error("Failed to build operation list: {}", e.getMessage());
            throw new IllegalStateException("Failed to build operation list", e);
        }
    }

    private List<List<Operation>> partitionedOperations(String chatId) {
        List<Operation> operations = communicatorService.getOperations(chatId);
        if (operations.isEmpty()) {
            return Collections.emptyList();
        }
        return ListUtils.partition(operations, 20);
    }

    private SendMessage chooseReply(String chatId) {
        List<List<Operation>> partitionedOperations = partitionedOperations(chatId);
        if (partitionedOperations.isEmpty()) {
            return basicReply(chatId, NO_OPERATIONS.getText());
        }
        return inlineKeyboardReply(chatId, partitionedOperations);
    }

    private String buildOperationsString(List<Operation> operations, String chatId,
                                         Map<UUID, Currency> tempCurrencies, Map<UUID, Category> tempCategories) {
        StringBuilder sb = new StringBuilder();
        for (Operation operation : operations) {
            sb.append('\n');
            sb.append("Id: ").append(operation.getUuid()).append('\n');
            sb.append("Дата: ")
                    .append(operation.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append('\n');
            sb.append("Описание: ").append(operation.getDescription()).append('\n');

            Category category = tempCategories.get(operation.getCategory());
            if (category == null) {
                category = communicatorService.getCategory(operation.getCategory(), chatId);
                tempCategories.put(category.getId(), category);
            }
            sb.append("Категория: ").append(category.getTitle()).append('\n');

            sb.append("Сумма: ").append(operation.getValue()).append('\n');
            Currency currency = tempCurrencies.get(operation.getCurrency());
            if (currency == null) {
                currency = communicatorService.getCurrency(operation.getCurrency(), chatId);
                tempCurrencies.put(currency.getId(), currency);
            }
            sb.append("Валюта: ").append(currency.getTitle()).append('\n');
        }
        return sb.toString();
    }


    private SendMessage getInfo(Chat chat) {
        UUID accountId = chat.getChosenAccount();
        if (accountId == null) {
            throw new IllegalStateException(
                    String.format("Account id is null chat id %s chat state %s", chat.getId(), chat.getState()));
        }
        Account account = communicatorService.getAccount(accountId, chat.getId());
        Currency currency = communicatorService.getCurrency(account.getCurrency(), chat.getId());
        String text = String.format(
                "Id: %s\nНазвание: %s\nОписание: %s\nТип: %s\nБаланс: %s\nВалюта: %s",
                accountId,
                account.getTitle(),
                account.getDescription(),
                account.getType(),
                account.getBalance(),
                currency.getTitle());
        return basicReply(chat.getId(), text);
    }

    private SendMessage getBalance(Chat chat) {
        UUID accountId = chat.getChosenAccount();
        if (accountId == null) {
            throw new IllegalStateException(
                    String.format("Account id is null chat id %s chat state %s", chat.getId(), chat.getState()));
        }
        Account account = communicatorService.getAccount(accountId, chat.getId());
        Currency currency = communicatorService.getCurrency(account.getCurrency(), chat.getId());
        String text = String.format(
                "На данный момент, на вашем счете %s %s", account.getBalance(), currency.getTitle());
        return basicReply(chat.getId(), text);
    }

    private SendMessage basicReply(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(MenuState.ACCOUNT).get());
        return message;
    }

    private SendMessage inlineKeyboardReply(String chatId, List<List<Operation>> partitionedOperations) {
        SendMessage message = new SendMessage(chatId, OPERATION_CHOICE.getText());
        message.setReplyMarkup(new OperationInlineKeyboard().get(partitionedOperations));
        return message;
    }

    private SendMessage basicReplyNewState(String chatId, String text, MenuState state) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(state).get());
        return message;
    }
}
