package by.itacademy.telegram.view.handler.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallBackQueryHandler {

    BotApiMethod<?> handle(CallbackQuery callbackQuery);
}
