package by.itacademy.report.utils;

public enum Messages {

    IS_NULL("is null"),
    IS_EMPTY("is empty"),
    INVALID_FORMAT("invalid format");

    private final String text;

    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
