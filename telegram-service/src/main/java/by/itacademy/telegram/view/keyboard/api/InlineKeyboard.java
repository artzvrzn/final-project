package by.itacademy.telegram.view.keyboard.api;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface InlineKeyboard<T> {

    InlineKeyboardMarkup get(List<T> item);
}
