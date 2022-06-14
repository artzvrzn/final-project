package by.itacademy.account.model;

import by.itacademy.account.validation.anno.Exist;
import by.itacademy.account.validation.anno.ValidType;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Account extends BaseDto {

    @NotBlank(message = "not passed")
    private String title;
    @NotBlank(message = "not passed")
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal value;
    @NotNull(message = "not passed")
    @ValidType
    private AccountType type;
    @NotNull(message = "not passed")
    @Exist(Exist.Classifier.CURRENCY)
    private UUID currency;
}
