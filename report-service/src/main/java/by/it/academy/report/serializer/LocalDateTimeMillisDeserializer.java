package by.it.academy.report.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Log4j2
public class LocalDateTimeMillisDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext dc) {
        try {
            long millis = Long.parseLong(jp.getValueAsString());
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDateTime()
                    .truncatedTo(ChronoUnit.MILLIS);
        } catch (IOException | NumberFormatException e) {
            log.error("millis deserialization error", e);
            throw new IllegalArgumentException("Wrong time format");
        }
    }
}
