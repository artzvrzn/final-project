package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.model.constant.AccountType;
import by.itacademy.telegram.storage.AccountStateStorage;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.view.api.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;

import static by.itacademy.telegram.model.constant.Reply.*;

@Log4j2
@Component
public class CreateAccountMessageHandler implements MessageHandler {

    @Autowired
    private AccountStateStorage stateStorage;
    @Autowired
    private ChatService chatService;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private CommunicatorService communicatorService;
    @Autowired
    private FinanceTelegramBot bot;


    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        log.info("chat {} message {}", chatId, input);
        if (input.equals(ButtonType.SUB_GET_BACK.getText())) {
            chatService.updateState(chatId, MenuState.MAIN);
            stateStorage.delete(chat.getId());
            return basicReplyMainMenu(chatId, HEADER_MAIN.getText());
        }
        if (input.equals(ButtonType.SUB_START_AGAIN.getText())) {
            stateStorage.delete(chatId);
            bot.sendMessage(basicReply(chatId, NEW_ATTEMPT.getText()));
            return basicReply(chatId, ACCOUNT_TITLE.getText());
        }
        Account account = stateStorage.get(chatId);
        if (account == null) {
            account = new Account();
            account.setState(0);
            stateStorage.add(chatId, account);
        }
        handleAccount(account, message);
        generateReply(account, chatId);
        return null;
    }

    private void generateReply(Account account, String chatId) {
        switch (account.getState()) {
            case 0:
                bot.sendMessage(basicReply(chatId, ACCOUNT_TITLE.getText()));
                break;
            case 1:
                bot.sendMessage(basicReply(chatId, ACCOUNT_DESCRIPTION.getText()));
                break;
            case 2:
                bot.sendMessage(basicReply(chatId, ACCOUNT_TYPE.getText()));
                bot.sendMessage(basicReply(chatId, AccountType.getAvailableTypes()));
                break;
            case 3:
                bot.sendMessage(basicReply(chatId, ACCOUNT_CURRENCY.getText()));
                bot.sendMessage(basicReply(chatId, HandlerUtils.getAvailableCurrencies(communicatorService, chatId)));
                break;
            default:
        }
    }

    private void handleAccount(Account account, Message message) {
        String text = message.getText();
        String chatId = message.getChatId().toString();
        log.info("Account creation: chat id {}, account stage {}, input {}", chatId, account.getState(), text);
        if (ButtonType.contains(text)) {
            return;
        }
        switch (account.getState()) {
            case 0:
                account.setTitle(text);
                account.setState(1);
                break;
            case 1:
                account.setDescription(text);
                account.setState(2);
                break;
            case 2:
                AccountType type = AccountType.valueOfOrInvalid(text);
                if (type.equals(AccountType.INVALID)) {
                    bot.sendMessage(basicReply(chatId, ACCOUNT_WRONG_TYPE.getText()));
                    return;
                }
                account.setType(type);
                account.setState(3);
                break;
            case 3:
                Currency currency = communicatorService.getCurrency(message.getText(), chatId);
                if (currency == null) {
                    bot.sendMessage(basicReply(chatId, ACCOUNT_WRONG_CURRENCY.getText()));
                    break;
                }
                account.setCurrency(currency.getId());
                try {
                    communicatorService.postAccount(account, chatId);
                    bot.sendMessage(basicReply(chatId, ACCOUNT_SUCCESS.getText()));
                    account.setState(4);
                    break;
                } catch (HttpStatusCodeException exc) {
                    account.setState(4);
                    bot.sendMessage(basicReply(chatId, ACCOUNT_FAILED.getText()));
                    bot.sendMessage(basicReply(chatId, NEW_ATTEMPT_QUESTION.getText()));
                    break;
                }
            case 4:
                bot.sendMessage(basicReply(chatId, ACCOUNT_SUCCESS.getText()));
                break;
            case 5:
                bot.sendMessage(basicReply(chatId, ACCOUNT_FAILED.getText()));
                bot.sendMessage(basicReply(chatId, NEW_ATTEMPT_QUESTION.getText()));
                break;
            default:
                bot.sendMessage(basicReply(chatId, UNKNOWN_COMMAND.getText()));
        }
    }

    private SendMessage basicReply(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.ACCOUNT_CREATE));
    }

    private SendMessage basicReplyMainMenu(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.MAIN));
    }
}
