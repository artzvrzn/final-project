package by.itacademy.classifier.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

@JsonComponent
@Log4j2
public class LocalDateMillisSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate ld, JsonGenerator jg, SerializerProvider sp) {
        try {
            jg.writeNumber(ld.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        } catch (IOException e) {
            log.error("millis date serialization error", e);
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}
