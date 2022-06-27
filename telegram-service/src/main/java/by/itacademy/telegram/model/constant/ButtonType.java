package by.itacademy.telegram.model.constant;

public enum ButtonType {

    SUB_START_AGAIN("Начать заново"),
    SUB_GET_BACK("Вернуться"),
    MAIN_CREATE_ACCOUNT("Создать аккаунт"),
    MAIN_CHOOSE_ACCOUNT("Выбрать аккаунт"),
    MAIN_GET_ACCOUNTS("Список аккаунтов"),
    ACCOUNT_GET_INFO("Информация об аккаунте"),
    ACCOUNT_UPDATE_ACCOUNT("Обновить аккаунт"),
    ACCOUNT_GET_BALANCE("Проверить баланс"),
    ACCOUNT_CREATE_OPERATION("Создать операцию"),
    ACCOUNT_CHOOSE_OPERATION("Выбрать операцию"),
    ACCOUNT_GET_OPERATIONS("Список операций"),
    OPERATION_GET_INFO("Информация об операции"),
    OPERATION_UPDATE("Изменить операцию"),
    OPERATION_DELETE("Удалить операцию");

    private final String text;

    ButtonType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static boolean contains(String test) {
        for (ButtonType b : ButtonType.values()) {
            if (b.getText().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
