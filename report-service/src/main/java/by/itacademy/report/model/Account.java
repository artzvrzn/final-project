package by.itacademy.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Account {

    @JsonProperty("uuid")
    private UUID id;
    private BigDecimal balance;
    private UUID currency;
}
