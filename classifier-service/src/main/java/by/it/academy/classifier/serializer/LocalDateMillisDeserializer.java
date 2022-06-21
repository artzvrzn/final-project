package by.it.academy.classifier.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@JsonComponent
@Log4j2
public class LocalDateMillisDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext dc) {
        try {
            long millis = Long.parseLong(jp.getValueAsString());
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDate();
        } catch (NumberFormatException | IOException e) {
            log.error("millis date deserialization error", e);
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}
