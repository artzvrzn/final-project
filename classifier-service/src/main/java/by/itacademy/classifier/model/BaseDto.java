package by.itacademy.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseDto {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @JsonProperty(value = "dt_created", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
    @JsonProperty(value = "dt_updated", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;
}
