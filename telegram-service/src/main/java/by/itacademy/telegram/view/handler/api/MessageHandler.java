package by.itacademy.telegram.view.handler.api;

import by.itacademy.telegram.model.Chat;
import by.itacademy.telegram.model.constant.MenuState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {

    BotApiMethod<?> handle(Message message, Chat chat);
}
