package by.itacademy.mail.scheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduledMail {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @JsonProperty(value = "dt_create", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
    @JsonProperty(value = "dt_update", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;
    @NotNull
    @Valid
    private Schedule schedule;
    @NotNull
    @Valid
    private Mail mail;
}
