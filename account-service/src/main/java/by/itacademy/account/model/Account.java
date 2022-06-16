package by.itacademy.account.model;

import by.itacademy.account.validation.anno.Exist;
import by.itacademy.account.validation.anno.ValidType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({"uuid", "dt_created", "dt_updated", "title", "description", "balance", "type", "currency"})
public class Account extends BaseDto {

    @NotBlank(message = "not passed")
    private String title;
    @NotBlank(message = "not passed")
    private String description;
    @JsonProperty(value = "balance", access = JsonProperty.Access.READ_ONLY)
    private BigDecimal value;
    @NotNull(message = "not passed")
    @ValidType
    private AccountType type;
    @NotNull(message = "not passed")
    @Exist(Exist.Classifier.CURRENCY)
    private UUID currency;
}
