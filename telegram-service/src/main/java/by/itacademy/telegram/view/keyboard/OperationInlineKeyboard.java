package by.itacademy.telegram.view.keyboard;

import by.itacademy.telegram.model.Operation;
import by.itacademy.telegram.model.constant.CallbackData;
import by.itacademy.telegram.view.keyboard.api.InlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class OperationInlineKeyboard  {

    public InlineKeyboardMarkup get(List<List<Operation>> partitionedOperations) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (List<Operation> operations: partitionedOperations) {
            for (Operation operation : operations) {
                rowList.add(
                        getButton(
                                String.format("%s %s %s", operation.getDate(), operation.getDescription(), operation.getValue()),
                                CallbackData.OPERATION_ + operation.getUuid().toString()));
            }
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
