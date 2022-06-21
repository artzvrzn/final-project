package by.it.academy.report.model;

import by.it.academy.report.serializer.LocalDateMillisDeserializer;
import by.it.academy.report.serializer.LocalDateTimeMillisSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Operation {

    @JsonSerialize(using = LocalDateTimeMillisSerializer.class)
    @JsonDeserialize(using = LocalDateMillisDeserializer.class)
    private LocalDate date;
    private UUID category;
    private BigDecimal value;
    private UUID currency;
}
