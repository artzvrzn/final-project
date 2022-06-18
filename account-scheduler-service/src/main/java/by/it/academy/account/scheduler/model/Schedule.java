package by.it.academy.account.scheduler.model;

import by.it.academy.account.scheduler.validation.anno.FutureTime;
import by.it.academy.account.scheduler.validation.anno.StopAfterStart;
import by.it.academy.account.scheduler.validation.anno.ValidType;
import by.it.academy.account.scheduler.validation.groups.FirstOrder;
import by.it.academy.account.scheduler.validation.groups.SecondOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@GroupSequence({Schedule.class, FirstOrder.class, SecondOrder.class})
@StopAfterStart(groups = SecondOrder.class)
public class Schedule implements Serializable {

    @NotNull
    @FutureTime(groups = FirstOrder.class)
    private LocalDateTime startTime;
    @NotNull
    @FutureTime(groups = FirstOrder.class)
    private LocalDateTime stopTime;
    @Min(1)
    private int interval;
    @NotNull
    @ValidType
    private TimeUnit timeUnit;
}
