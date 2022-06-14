package by.itacademy.account.model;

import by.itacademy.account.validation.anno.Exist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Operation extends BaseDto {

    @NotNull(message = "not passed")
    private LocalDate date;
    @NotBlank(message = "not passed")
    private String description;
    @Exist(Exist.Classifier.CATEGORY)
    private UUID category;
    @NotNull(message = "not passed")
    private BigDecimal value;
    @Exist(Exist.Classifier.CURRENCY)
    private UUID currency;
}
