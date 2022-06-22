package by.itacademy.account.scheduler.converter;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Log4j2
public class StringMillisToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@Nullable String str) {
        try {
            long millis = Long.parseLong(str);
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDateTime();
        } catch (NumberFormatException | NullPointerException e) {
            log.error("ldt conversion error", e);
            throw new IllegalArgumentException("invalid date format");
        }
    }
}
