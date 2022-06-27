package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.model.constant.Reply;
import by.itacademy.telegram.storage.AccountStateStorage;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.AccountInlineKeyboard;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.view.api.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Log4j2
@Component
public class MainMenuMessageHandler implements MessageHandler {

    @Autowired
    private ChatService chatService;
    @Autowired
    private CommunicatorService communicatorService;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private MessageHandlerFactory handlerFactory;
    @Autowired
    private FinanceTelegramBot bot;
    @Autowired
    private AccountStateStorage accountStateStorage;

    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        log.info("chat {} message {}", chatId, input);
        if (input.equals(ButtonType.MAIN_CREATE_ACCOUNT.getText())) {
            chatService.updateState(chat.getId(), MenuState.ACCOUNT_CREATE);
            bot.sendMessage(basicReplyNewState(chatId, Reply.HEADER_ACCOUNT_CREATE.getText(), MenuState.ACCOUNT_CREATE));
            return handlerFactory.delegate(message);
        }
        if (input.equals(ButtonType.MAIN_GET_ACCOUNTS.getText())) {
            return getAccounts(chatId);
        }
        if (input.equals(ButtonType.MAIN_CHOOSE_ACCOUNT.getText())) {
            return chooseReply(chatId);
        }
        return basicReply(chatId, Reply.UNKNOWN_COMMAND.getText());
    }

    private SendMessage getAccounts(String chatId) {
        List<Account> accounts = communicatorService.getAccounts(chatId);
        if (accounts.isEmpty()) {
            return basicReply(chatId, Reply.NO_ACCOUNTS.getText());
        }
        StringBuilder sb = new StringBuilder();
        for (Account account: accounts) {
            sb.append('\n');
            sb.append("id: ").append(account.getUuid()).append('\n');
            sb.append("название: ").append(account.getTitle()).append('\n');
            sb.append("описание: ").append(account.getDescription()).append('\n');
            Currency currency = communicatorService.getCurrency(account.getCurrency(), chatId);
            sb.append("валюта: ").append(currency.getTitle());
            sb.append('\n');
        }
        return basicReply(chatId, sb.toString());
    }

    private SendMessage basicReply(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.get(MenuState.MAIN).get());
        return sendMessage;
    }

    private SendMessage basicReplyNewState(String chatId, String text, MenuState state) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(state).get());
        return message;
    }

    private SendMessage chooseReply(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        List<Account> accounts = communicatorService.getAccounts(chatId);
        if (accounts.isEmpty()) {
            sendMessage.setText(Reply.NO_ACCOUNTS.getText());
        } else {
            sendMessage.setText(Reply.ACCOUNT_CHOICE.getText());
        }
        sendMessage.setReplyMarkup(new AccountInlineKeyboard().get(accounts));
        return sendMessage;
    }
}
