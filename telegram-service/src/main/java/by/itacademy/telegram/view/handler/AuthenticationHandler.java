package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.model.Authentication;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.model.constant.Reply;
import by.itacademy.telegram.storage.AuthenticationStateStorage;
import by.itacademy.telegram.view.api.AuthenticationService;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.handler.api.MessageHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import by.itacademy.telegram.view.keyboard.MainMenuKeyboard;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Log4j2
@Component
public class AuthenticationHandler implements MessageHandler {

    @Autowired
    private AuthenticationStateStorage stateStorage;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private KeyboardFactory keyboardFactory;
    @Autowired
    private FinanceTelegramBot bot;


    @Override
    public BotApiMethod<?> handle(Message message, Chat chat) {
        String chatId = message.getChatId().toString();
        Authentication authentication = stateStorage.get(chatId);
        if (authentication == null) {
            authentication = new Authentication();
            authentication.setState(-1);
            stateStorage.add(chatId, authentication);
        }
        handleAuthentication(authentication, message, chat); // handle should be before reply !
        generateReply(authentication, chatId);
        return null;
    }

    private void handleAuthentication(Authentication authentication, Message message, Chat chat) {
        String chatId = chat.getId();
        String input = message.getText();
        if (ButtonType.contains(input) || input.equals("/start")) {
            return;
        }
        log.info("chat id {}, message {}", chatId, input);
        switch (authentication.getState()) {
            case 0:
                authentication.setLogin(input);
                authentication.setState(1);
                break;
            case 1:
                authentication.setPassword(input);
                authentication.setState(2);
                try {
                    String token = authenticationService.getToken(authentication);
                    chat.setState(MenuState.MAIN);
                    chat.setJwtToken(token);
                    chatService.update(chatId, chat);
                    bot.sendMessage(basicReplyNewState(chatId, Reply.AUTH_SUCCESS.getText(), MenuState.MAIN));
                    stateStorage.delete(chatId);
                    break;
                } catch (SecurityException exc) {
                    authentication.setState(0);
                    bot.sendMessage(basicReply(chatId, Reply.AUTH_FAILED.getText()));
                    break;
                }
            default:
        }

    }
    private void generateReply(Authentication authentication, String chatId) {
        switch (authentication.getState()) {
            case -1:
                bot.sendMessage(basicReply(chatId, Reply.HEADER_AUTH.getText()));
                authentication.setState(0);
            case 0:
                bot.sendMessage(basicReply(chatId, Reply.AUTH_USER.getText()));
                break;
            case 1:
                bot.sendMessage(basicReply(chatId, Reply.AUTH_PASSWORD.getText()));
                break;
            default:
        }
    }

    private SendMessage basicReplyNewState(String chatId, String text, MenuState state) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboardFactory.get(state).get());
        return message;
    }

    private SendMessage basicReply(String chatId, String text) {
        return new SendMessage(chatId, text);
    }
}
