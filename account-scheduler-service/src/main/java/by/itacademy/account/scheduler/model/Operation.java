package by.itacademy.account.scheduler.model;

import by.itacademy.account.scheduler.validation.groups.FirstOrder;
import by.itacademy.account.scheduler.validation.groups.SecondOrder;
import by.itacademy.account.scheduler.validation.anno.CurrencyBelongsAccount;
import by.itacademy.account.scheduler.validation.anno.Exist;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@GroupSequence({Operation.class, FirstOrder.class, SecondOrder.class})
@CurrencyBelongsAccount(groups = SecondOrder.class)
public class Operation implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate date;
    @NotNull
    @Exist(value = Exist.ServiceType.ACCOUNT, groups = FirstOrder.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID account;
    @NotBlank
    private String description;
    @JsonProperty(required = true)
    private BigDecimal value;
    @NotNull
    @Exist(value = Exist.ServiceType.CURRENCY,groups = FirstOrder.class)
    private UUID currency;
    @NotNull
    @Exist(value = Exist.ServiceType.CATEGORY, groups = FirstOrder.class)
    private UUID category;
}
