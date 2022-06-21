package by.it.academy.report.utils;

public enum Messages {

    IS_NULL("is null"),
    IS_EMPTY("is empty"),
    INVALID_FORMAT("invalid format");

    private String text;

    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}