package by.itacademy.account.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
