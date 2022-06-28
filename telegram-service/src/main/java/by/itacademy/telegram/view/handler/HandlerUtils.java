package by.itacademy.telegram.view.handler;

import by.itacademy.telegram.model.Category;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.view.api.CommunicatorService;
import by.itacademy.telegram.view.keyboard.api.Keyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.stream.Collectors;

public class HandlerUtils {

    public static SendMessage basicReply(String chatId, String text, Keyboard keyboard) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(keyboard.get());
        return message;
    }

    public static String getAvailableCurrencies(CommunicatorService communicatorService, String chatId) {
        return communicatorService.getCurrencies(chatId)
                .stream()
                .map(Currency::getTitle)
                .collect(Collectors.joining("\n"));
    }

    public static String getAvailableCategories(CommunicatorService communicatorService, String chatId) {
        return communicatorService.getCategories(chatId)
                .stream()
                .map(Category::getTitle)
                .collect(Collectors.joining("\n"));
    }
}
