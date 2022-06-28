package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.model.constant.CallbackData;
import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.model.constant.Reply;
import by.itacademy.telegram.view.api.ChatService;
import by.itacademy.telegram.view.handler.api.CallBackQueryHandler;
import by.itacademy.telegram.view.keyboard.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.UUID;

@Component
public class CallBackQueryHandlerImpl implements CallBackQueryHandler {

    @Autowired
    private ChatService chatService;
    @Autowired
    private KeyboardFactory keyboardFactory;

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();
        if (data.startsWith(CallbackData.ACCOUNT_.name())) {
            String id = data.replaceAll(".+_", "");
            chatService.updateAccount(chatId, UUID.fromString(id));
            SendMessage sendMessage = new SendMessage(chatId, Reply.ACCOUNT_CHOICE_MADE.getText());
            sendMessage.setReplyMarkup(keyboardFactory.get(MenuState.ACCOUNT).get());
            return sendMessage;
        }
        if (data.startsWith(CallbackData.OPERATION_.name())) {
            String id = data.replaceAll(".+_", "");
            SendMessage sendMessage = new SendMessage(chatId, Reply.OPERATION_CHOICE_MADE.getText());
            chatService.updateOperation(chatId, UUID.fromString(id));
            sendMessage.setReplyMarkup(keyboardFactory.get(MenuState.OPERATION).get());
            return sendMessage;
        }
        return null;
    }
}
