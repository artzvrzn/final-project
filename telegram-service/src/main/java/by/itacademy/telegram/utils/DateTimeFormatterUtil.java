package by.itacademy.telegram.utils;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormatterUtil {

    private DateTimeFormatterUtil() {}

    public static DateTimeFormatter getUniversalDateFormatter() {
        return DateTimeFormatter.ofPattern("[dd-MM-yyyy][dd.MM.yyyy][yyyy-MM-dd][dd/MM/yyyy]");
    }
}
