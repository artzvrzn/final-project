package by.itacademy.telegram.view.keyboard;

import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.constant.CallbackData;
import by.itacademy.telegram.view.keyboard.api.InlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class AccountInlineKeyboard implements InlineKeyboard<Account> {

    @Override
    public InlineKeyboardMarkup get(List<Account> accounts) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (Account account: accounts) {
            rowList.add(
                    getButton(account.getTitle(), CallbackData.ACCOUNT_ + account.getUuid().toString()));
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}
