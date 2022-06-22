package by.itacademy.account.scheduler.converter;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Log4j2
public class StringMillisToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String str) {
        try {
            long millis = Long.parseLong(str);
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDate();
        } catch (NumberFormatException | NullPointerException e) {
            log.error("ldt conversion error", e);
            throw new IllegalArgumentException("invalid date format");
        }
    }
}
