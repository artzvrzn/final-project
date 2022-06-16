package by.itacademy.account.model;

import by.itacademy.account.serializer.LocalDateTimeMillisDeserializer;
import by.itacademy.account.serializer.LocalDateTimeMillisSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseDto {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @JsonProperty(value = "dt_created")
    private LocalDateTime created;
    @JsonProperty(value = "dt_updated")
    private LocalDateTime updated;
}
