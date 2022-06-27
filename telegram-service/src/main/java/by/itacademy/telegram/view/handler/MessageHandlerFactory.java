package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.model.constant.MenuState;
import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.view.api.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandlerFactory {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ApplicationContext applicationContext;

    public BotApiMethod<?> delegate(Message message) {
        Chat chat = chatService.get(message);
        if (chat == null) {
            chat = new Chat();
            chat.setId(message.getChatId().toString());
            chat.setState(MenuState.AUTH);
            chatService.create(chat);
        }
        if (message.getText().equals("/start")) {
            chat.setState(MenuState.AUTH);
            chatService.updateState(chat.getId(), MenuState.AUTH);
            return applicationContext.getBean(AuthenticationHandler.class).handle(message, chat);
        }
        switch (chat.getState()) {
            case MAIN:
                return applicationContext.getBean(MainMenuMessageHandler.class).handle(message, chat);
            case ACCOUNT:
                return applicationContext.getBean(AccountMessageHandler.class).handle(message, chat);
            case ACCOUNT_CREATE:
                return applicationContext.getBean(CreateAccountMessageHandler.class).handle(message, chat);
            case ACCOUNT_UPDATE:
                return applicationContext.getBean(UpdateAccountMessageHandler.class).handle(message, chat);
            case OPERATION:
                return applicationContext.getBean(OperationMessageHandler.class).handle(message, chat);
            case OPERATION_CREATE:
                return applicationContext.getBean(CreateOperationMessageHandler.class).handle(message, chat);
            case OPERATION_UPDATE:
                return applicationContext.getBean(UpdateOperationMessageHandler.class).handle(message, chat);
            default:
                return applicationContext.getBean(AuthenticationHandler.class).handle(message, chat);
        }
    }
}
