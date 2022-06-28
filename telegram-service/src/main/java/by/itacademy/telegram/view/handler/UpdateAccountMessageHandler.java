package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.AccountType;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.storage.AccountStateStorage;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.UUID;

import static by.itacademy.telegram.model.constant.Reply.*;
import static by.itacademy.telegram.model.constant.Reply.UNKNOWN_COMMAND;

@Log4j2
@Component
public class UpdateAccountMessageHandler implements MessageHandler {

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
        if (message.getText().equals(ButtonType.SUB_GET_BACK.getText())) {
            stateStorage.delete(chat.getId());
            chatService.updateState(chat.getId(), MenuState.ACCOUNT);
            return basicReplyMainMenu(chat.getId(), HEADER_ACCOUNT.getText());
        }
        if (message.getText().equals(ButtonType.SUB_START_AGAIN.getText())) {
            stateStorage.delete(chat.getId());
            bot.sendMessage(basicReply(chat.getId(), NEW_ATTEMPT.getText()));
            return basicReply(chat.getId(), ACCOUNT_UPDATE_TITLE.getText());
        }
        Account account = stateStorage.get(chat.getId());
        if (account == null) {
            UUID accountId = chat.getChosenAccount();
            account = communicatorService.getAccount(accountId, chat.getId());
            account.setState(0);
            stateStorage.add(chat.getId(), account);
        }
        handleAccount(account, message);
        generateReply(account, chat.getId());
        return null;
    }

    private void generateReply(Account account, String chatId) {
        switch (account.getState()) {
            case 0:
                bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_TITLE.getText()));
                break;
            case 1:
                bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_DESCRIPTION.getText()));
                break;
            case 2:
                bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_TYPE.getText()));
                bot.sendMessage(basicReply(chatId, AccountType.getAvailableTypes()));
                break;
            default:
        }
    }

    private void handleAccount(Account account, Message message) {
        String text = message.getText();
        String chatId = message.getChatId().toString();
        if (ButtonType.contains(text)) {
            return;
        }
        log.info("Account update: chat id {}, account stage {}, input {}", chatId, account.getState(), text);
        switch (account.getState()) {
            case 0:
                if (!isSlash(text)) account.setTitle(text);
                account.setState(1);
                break;
            case 1:
                if (!isSlash(text)) account.setDescription(text);
                account.setState(2);
                break;
            case 2:
                if (!isSlash(text)) {
                    AccountType type = AccountType.valueOfOrInvalid(text);
                    if (type.equals(AccountType.INVALID)) {
                        bot.sendMessage(basicReply(chatId, ACCOUNT_WRONG_TYPE.getText()));
                        break;
                    }
                    account.setType(type);
                }
                try {
                    communicatorService.updateAccount(account, chatId);
                    bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_SUCCESS.getText()));
                    account.setState(3);
                    break;
                } catch (HttpStatusCodeException exc) {
                    bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_FAILED.getText()));
                    bot.sendMessage(basicReply(chatId, NEW_ATTEMPT_QUESTION.getText()));
                    account.setState(4);
                    break;
                }
            case 3:
                bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_SUCCESS.getText()));
                break;
            case 4:
                bot.sendMessage(basicReply(chatId, ACCOUNT_UPDATE_FAILED.getText()));
                bot.sendMessage(basicReply(chatId, NEW_ATTEMPT_QUESTION.getText()));
                break;
            default:
                bot.sendMessage(basicReply(chatId, UNKNOWN_COMMAND.getText()));
        }
    }

    private boolean isSlash(String text) {
        return text.equals("/");
    }

    private SendMessage basicReply(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.ACCOUNT_CREATE));
    }

    private SendMessage basicReplyMainMenu(String chatId, String text) {
        return HandlerUtils.basicReply(chatId, text, keyboardFactory.get(MenuState.MAIN));
    }
}
