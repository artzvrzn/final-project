package by.itacademy.telegram.model.constant;

public enum Reply {

    GREET("Привет привет"),
    HEADER_AUTH("Страница авторизации"),
    HEADER_MAIN("Главное меню"),
    HEADER_ACCOUNT("Страница аккаунта"),
    HEADER_ACCOUNT_CREATE("Создание аккаунта"),
    HEADER_OPERATION("Страница операции"),
    HEADER_OPERATION_UPDATE("Изменение операции"),
    HEADER_ACCOUNT_UPDATE("Обновление аккаунта"),
    HEADER_OPERATION_CREATE("Создание операции"),
    AUTH_USER("Введите имя пользователя"),
    AUTH_PASSWORD("Введите пароль"),
    AUTH_SUCCESS("Вы успешно авторизованы"),
    AUTH_FAILED("Неверное имя или пароль"),
    ACCOUNT_TITLE("Введите название для аккаунта"),
    ACCOUNT_DESCRIPTION("Введите описание аккаунта"),
    ACCOUNT_TYPE("Введите тип аккаунта"),
    ACCOUNT_WRONG_TYPE("Неверный тип аккаунта!"),
    ACCOUNT_CURRENCY("Введите валюту аккаунта"),
    ACCOUNT_WRONG_CURRENCY("Нет такой валюты!"),
    ACCOUNT_SUCCESS("Аккаунт успешно создан!"),
    ACCOUNT_FAILED("Не удалось создать аккаунт"),
    ACCOUNT_UPDATE_TITLE("Введите новое название (\"/\" чтобы оставить прежним)"),
    ACCOUNT_UPDATE_DESCRIPTION("Введите новое описание (\"/\" чтобы оставить прежнее)"),
    ACCOUNT_UPDATE_TYPE("Введите новый тип (\"/\" чтобы оставить прежним)"),
    ACCOUNT_UPDATE_SUCCESS("Аккаунт успешно обновлен!"),
    ACCOUNT_UPDATE_FAILED("Не удалось обновить аккаунт"),
    ACCOUNT_CHOICE("Выберите аккаунт"),
    OPERATION_DATE("Введите дату операции"),
    OPERATION_UPDATE_DATE("Введите дату операции (\"/\" чтобы оставить прежнюю)"),
    OPERATION_DESCRIPTION("Введите описании операции"),
    OPERATION_UPDATE_DESCRIPTION("Введите описании операции (\"/\" чтобы оставить прежнее)"),
    OPERATION_CATEGORY("Выберите подходящую категорию из предложенных"),
    OPERATION_UPDATE_CATEGORY("Выберите подходящую категорию из предложенных (\"/\" чтобы оставить прежнюю)"),

    OPERATION_WRONG_CATEGORY("Неверно указана категория"),
    OPERATION_VALUE("Введите сумму операции"),
    OPERATION_UPDATE_VALUE("Введите сумму операции (\"/\" чтобы оставить прежнюю)"),

    OPERATION_WRONG_VALUE("Неверно указана сумма операции"),
    OPERATION_SUCCESS("Операция успешно добавлена!"),
    OPERATION_FAILED("Не удалось добавить операцию"),
    OPERATION_CHOICE("Выберите операцию"),
    OPERATION_DELETE_SUCCESS("Операция удалена"),
    OPERATION_DELETE_FAILED("Не удалось удалить операцию"),
    OPERATION_UPDATE_SUCCESS("Операция обновлена"),
    OPERATION_UPDATE_FAILED("Не удалось обновить операцию"),
    NO_ACCOUNTS("У вас еще нет аккаунтов"),
    NO_OPERATIONS("У вас еще нет операций"),
    NEW_ATTEMPT_QUESTION("Хотите начать заново?"),
    NEW_ATTEMPT("Попробуем еще раз!"),
    WRONG_DATE_FORMAT("Неверный формат даты!"),
    UNKNOWN_COMMAND("Неизвестная команда"),
    EXCEPTION("Что-то пошло не так");

    private final String text;

    Reply(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
