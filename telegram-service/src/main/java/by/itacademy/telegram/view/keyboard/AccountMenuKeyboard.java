package by.itacademy.telegram.view.keyboard;

import by.itacademy.telegram.model.constant.ButtonType;
import by.itacademy.telegram.view.keyboard.api.Keyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMenuKeyboard implements Keyboard {

    @Override
    public ReplyKeyboard get() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonType.ACCOUNT_GET_INFO.getText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonType.ACCOUNT_UPDATE_ACCOUNT.getText()));
        row2.add(new KeyboardButton(ButtonType.ACCOUNT_GET_BALANCE.getText()));
        row2.add(new KeyboardButton(ButtonType.ACCOUNT_CREATE_OPERATION.getText()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(ButtonType.ACCOUNT_CHOOSE_OPERATION.getText()));
        row3.add(new KeyboardButton(ButtonType.ACCOUNT_GET_OPERATIONS.getText()));
        row3.add(new KeyboardButton(ButtonType.SUB_GET_BACK.getText()));
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        keyboardRowList.add(row3);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        return keyboardMarkup;
    }
}
