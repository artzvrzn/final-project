package by.itacademy.telegram.view.keyboard;

import by.itacademy.telegram.view.keyboard.api.Keyboard;
import by.itacademy.telegram.model.constant.MenuState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class KeyboardFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public Keyboard get(MenuState menuState) {
        switch (menuState) {
            case ACCOUNT_CREATE:
            case ACCOUNT_UPDATE:
            case OPERATION_UPDATE:
            case OPERATION_CREATE:
                return applicationContext.getBean(SubMenuKeyboard.class);
            case MAIN:
                return applicationContext.getBean(MainMenuKeyboard.class);
            case ACCOUNT:
                return applicationContext.getBean(AccountMenuKeyboard.class);
            case OPERATION:
                return applicationContext.getBean(OperationMenuKeyboard.class);
            default:
                return applicationContext.getBean(MainMenuKeyboard.class);
        }
    }

}
