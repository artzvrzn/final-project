package by.itacademy.telegram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Operation implements Stateful {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID uuid;
    private LocalDate date;
    @JsonProperty("dt_updated")
    private long updated;
    private String description;
    private UUID category;
    private BigDecimal value;
    private UUID currency;
    @JsonIgnore
    private int state;
    @JsonIgnore
    private LocalDateTime instantiated = LocalDateTime.now();
}
