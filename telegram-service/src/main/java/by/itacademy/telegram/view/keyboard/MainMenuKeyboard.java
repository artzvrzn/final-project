package by.itacademy.telegram.view.keyboard;

import by.itacademy.telegram.view.keyboard.api.Keyboard;
import by.itacademy.telegram.model.constant.ButtonType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuKeyboard implements Keyboard {

    @Override
    public ReplyKeyboard get() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonType.MAIN_CREATE_ACCOUNT.getText()));
        row1.add(new KeyboardButton(ButtonType.MAIN_CHOOSE_ACCOUNT.getText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonType.MAIN_GET_ACCOUNTS.getText()));
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        return keyboardMarkup;
    }
}
