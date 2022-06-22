package by.itacademy.account.scheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduledOperation implements Serializable {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @JsonProperty(value = "dt_create", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
    @JsonProperty(value = "dt_update", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;
    @NotNull(message = "not passed")
    @Valid
    private Operation operation;
    @NotNull(message = "not passed")
    @Valid
    private Schedule schedule;
}
