package by.it.academy.account.scheduler.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Date convert(LocalDateTime ldt) {
        return Date.from(ldt.atOffset(ZoneOffset.UTC).toInstant());
    }
}
