package by.it.academy.classifier.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonComponent
@Log4j2
public class LocalDateTimeMillisSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime ldt, JsonGenerator jg, SerializerProvider sp) {
        try {
            jg.writeNumber(ldt.toInstant(ZoneOffset.UTC).toEpochMilli());
        } catch (IOException e) {
            log.error("millis serialization error", e);
            throw new IllegalArgumentException("Wrong time format");
        }
    }
}
